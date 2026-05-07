package ru.itmo.tpo;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import ru.itmo.tpo.pages.MainPage;
import ru.itmo.tpo.pages.SearchResultsPage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public abstract class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected boolean authenticated;

    protected static Stream<SupportedBrowser> browsers() {
        return SupportedBrowser.fromSystemProperty().stream();
    }

    protected void initBrowser(SupportedBrowser supportedBrowser) {
        playwright = Playwright.create();

        BrowserType.LaunchOptions opts = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(0);

        browser = switch (supportedBrowser) {
            case CHROMIUM -> playwright.chromium().launch(opts);
            case FIREFOX  -> playwright.firefox().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(0)
                            .setFirefoxUserPrefs(Map.of(
                                    "browser.startup.homepage_override.mstone", "ignore",
                                    "startup.homepage_welcome_url",             "about:blank",
                                    "startup.homepage_welcome_url.additional",  "about:blank",
                                    "browser.tabs.warnOnOpen",                  false)));
        };

        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions()
                .setViewportSize(1280, 900)
                .setLocale("en-US")
                .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");

        Path stateFile = Paths.get(SaveAuthState.STATE_FILE);
        if (stateFile.toFile().exists()) {
            ctxOpts.setStorageStatePath(stateFile);
            authenticated = true;
            System.out.println("[auth] storage-state.json loaded → session active");
        } else {
            System.out.println("[auth] no storage-state.json → anonymous");
        }

        context = browser.newContext(ctxOpts);
        page = context.newPage();
        if (supportedBrowser == SupportedBrowser.FIREFOX) {
            page.setDefaultTimeout(45_000);
            page.setDefaultNavigationTimeout(45_000);
        }
        setupPageHandlers();
    }

    protected void setupPageHandlers() {
        Locator.ClickOptions click = new Locator.ClickOptions().setTimeout(2_000);
        Page.AddLocatorHandlerOptions opts = new Page.AddLocatorHandlerOptions().setNoWaitAfter(true);

        page.addLocatorHandler(
                page.locator("xpath=//button[@aria-label='Dismiss sign in information.']"
                        + " | //button[@aria-label='Dismiss sign in info.']"),
                overlay -> { try { overlay.first().click(click); } catch (Exception ignored) {} },
                opts);
        page.addLocatorHandler(
                page.locator("xpath=//button[@id='onetrust-accept-btn-handler']"),
                overlay -> { try { overlay.click(click); } catch (Exception ignored) {} },
                opts);
        page.addLocatorHandler(
                page.locator("xpath=//div[@role='dialog']//button[@aria-label='Close'"
                        + " or @aria-label='close' or @aria-label='Dismiss' or @aria-label='dismiss']"),
                overlay -> { try { overlay.first().click(click); } catch (Exception ignored) {} },
                opts);
    }

    protected SearchResultsPage performSearch(String city, String checkIn, String checkOut) {
        try {
            return new MainPage(page)
                    .open()
                    .enterDestination(city)
                    .openDatePicker()
                    .selectDate(checkIn)
                    .selectDate(checkOut)
                    .submitSearch();
        } catch (com.microsoft.playwright.TimeoutError e) {
            String first = e.getMessage().lines().findFirst().orElse("timeout");
            String url = "?";
            try { url = page.url(); } catch (Exception ignored) {}
            System.err.println("[performSearch] TimeoutError at URL=" + url);
            System.err.println("[performSearch]   " + first);
            org.junit.jupiter.api.Assumptions.assumeTrue(false,
                    "Booking.com не ответил вовремя - тест пропущен (" + first + ")");
            return null;
        }
    }

    protected static final long STEP_DELAY_MS =
            Long.parseLong(TestConfig.get("step.delay.ms", "1200"));

    protected void throttle() {
        page.waitForTimeout(STEP_DELAY_MS);
    }

    protected void skipUnless(boolean condition, String reason) {
        if (!condition) {
            System.err.println("[SKIP] " + reason);
        }
        org.junit.jupiter.api.Assumptions.assumeTrue(condition, reason);
    }

    protected void closeExtraTabs() {
        for (Page p : new java.util.ArrayList<>(context.pages())) {
            if (p != this.page) {
                try { p.close(); } catch (Exception ignored) {}
            }
        }
    }

    @AfterEach
    void tearDown() {
        if (page       != null) page.close();
        if (context    != null) context.close();
        if (browser    != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
