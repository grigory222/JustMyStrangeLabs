package ru.itmo.tpo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MainPage extends BasePage {

    private static final String URL = "https://www.booking.com";

    private static final String XP_DESTINATION_INPUT =
            "//div[@data-testid='destination-container']//input[@name='ss']";

    private static final String XP_SUGGESTION_FMT =
            "//div[@data-testid='destination-container']"
            + "//li[@data-testid='autocomplete-result' and contains(., '%s')]";

    private static final String XP_DATES_CONTAINER =
            "//button[@data-testid='searchbox-dates-container']";

    private static final String XP_DATEPICKER_CALENDAR =
            "//div[@data-testid='searchbox-datepicker-calendar']";

    private static final String XP_NEXT_MONTH =
            "//button[@aria-label='Next month']";

    private static final String XP_SEARCH_SUBMIT =
            "//div[@data-testid='searchbox-layout-wide']//button[@type='submit']";

    private static final String XP_COOKIE_ACCEPT =
            "//button[@id='onetrust-accept-btn-handler']";

    private static final String XP_GENIUS_DISMISS =
            "//button[@aria-label='Dismiss sign in information.']";

    public MainPage(Page page) {
        super(page);
    }

    public MainPage open() {
        navigate(URL);
        dismissAndWaitGone(XP_COOKIE_ACCEPT, 3_000);
        dismissIfPresent(XP_GENIUS_DISMISS, 1_500);
        try { page.keyboard().press("Escape"); } catch (Exception ignored) {}
        xpathFirst(XP_DESTINATION_INPUT)
                .waitFor(new Locator.WaitForOptions().setTimeout(20_000));
        return this;
    }

    private void dismissAndWaitGone(String xpath, int timeoutMs) {
        try {
            Locator btn = xpathFirst(xpath);
            btn.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
            btn.click();
            btn.waitFor(new Locator.WaitForOptions()
                    .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN)
                    .setTimeout(2_000));
        } catch (Exception ignored) {}
    }

    private void dismissIfPresent(String xpath, int timeoutMs) {
        try {
            Locator btn = xpathFirst(xpath);
            btn.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
            btn.click();
        } catch (Exception ignored) {}
    }

    public MainPage enterDestination(String destination) {
        Locator input = xpathFirst(XP_DESTINATION_INPUT);
        input.waitFor();
        input.click();
        input.fill(destination);

        Locator suggestion = xpathFirst(String.format(XP_SUGGESTION_FMT, destination));
        try {
            suggestion.waitFor(new Locator.WaitForOptions().setTimeout(3_000));
            suggestion.click();
            return this;
        } catch (Exception ignored) {}

        dismissIfPresent(XP_GENIUS_DISMISS, 1_500);
        input.click();
        input.fill(destination);
        try {
            suggestion.waitFor(new Locator.WaitForOptions().setTimeout(3_000));
            suggestion.click();
        } catch (Exception ignored) {}
        return this;
    }

    public MainPage openDatePicker() {
        Locator calendar = xpathFirst(XP_DATEPICKER_CALENDAR);
        try {
            calendar.waitFor(new Locator.WaitForOptions().setTimeout(2_000));
            return this;
        } catch (Exception ignored) {}
        xpathFirst(XP_DATES_CONTAINER).click();
        calendar.waitFor(new Locator.WaitForOptions().setTimeout(8_000));
        return this;
    }

    public MainPage selectDate(String isoDate) {
        Locator calendar = xpathFirst(XP_DATEPICKER_CALENDAR);
        Locator nextBtn  = xpathFirst(XP_NEXT_MONTH);
        Locator dayCell  = calendar.locator("[data-date='" + isoDate + "']");

        for (int i = 0; i < 6 && dayCell.count() == 0; i++) {
            try {
                nextBtn.waitFor(new Locator.WaitForOptions().setTimeout(2_000));
                nextBtn.click();
                dayCell.first().waitFor(new Locator.WaitForOptions().setTimeout(2_000));
            } catch (Exception ignored) {}
        }

        dayCell.first().waitFor(new Locator.WaitForOptions().setTimeout(5_000));
        page.evaluate(
                "(d) => document.querySelector("
                + "'[data-testid=\"searchbox-datepicker-calendar\"] [data-date=\"'+d+'\"]')?.click()",
                isoDate);
        return this;
    }

    public SearchResultsPage submitSearch() {
        dismissIfPresent(XP_GENIUS_DISMISS, 1_500);
        Locator btn = xpathFirst(XP_SEARCH_SUBMIT);
        btn.waitFor();
        btn.click();

        try {
            page.waitForURL(url ->
                            url.contains("searchresults")
                            || url.contains("/city/")
                            || url.contains("/hotel/")
                            || (url.contains("booking.com")
                                && !url.matches("https://www\\.booking\\.com/?")),
                    new Page.WaitForURLOptions().setTimeout(20_000));
        } catch (com.microsoft.playwright.TimeoutError ignored) {
            try {
                page.waitForLoadState(
                        com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                        new Page.WaitForLoadStateOptions().setTimeout(5_000));
            } catch (Exception ignored2) {}
        }
        return new SearchResultsPage(page);
    }
}
