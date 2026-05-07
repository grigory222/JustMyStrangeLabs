package ru.itmo.tpo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HotelDetailsPage extends BasePage {

    private static final String XP_HOTEL_NAME =
            "//h1";

    private static final String XP_HOTEL_ADDRESS =
            "//*[@data-testid='PropertyHeaderAddressDesktop-wrapper']";

    private static final String XP_BOOKING_DATE_FIELD =
            "//*[@data-testid='date-display-field-start']";

    private static final String XP_AVAILABILITY_ANCHOR =
            "//*[@id='availability_target']";

    private static final String XP_ROOM_QTY_SELECT =
            "//select[@data-testid='select-room-trigger']";

    private static final String XP_RESERVE_SUBMIT =
            "//*[@data-testid='reservation-summary']//button[@type='submit']";

    public HotelDetailsPage(Page page) {
        super(page);
    }

    public String getHotelName() {
        Locator h1 = xpathFirst(XP_HOTEL_NAME);
        h1.waitFor(new Locator.WaitForOptions().setTimeout(15_000));
        return h1.textContent().trim();
    }

    public String getAddress() {
        try {
            Locator el = xpathFirst(XP_HOTEL_ADDRESS);
            el.waitFor(new Locator.WaitForOptions().setTimeout(5_000));
            String text = el.textContent();
            return text == null ? "" : text.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getBookingCheckInDate() {
        try {
            Locator el = xpathFirst(XP_BOOKING_DATE_FIELD);
            el.waitFor(new Locator.WaitForOptions().setTimeout(5_000));
            return el.textContent().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public int getAvailableRoomTypeCount() {
        try {
            Locator anchor = xpathFirst(XP_AVAILABILITY_ANCHOR);
            anchor.waitFor(new Locator.WaitForOptions().setTimeout(8_000));
            anchor.scrollIntoViewIfNeeded();
        } catch (Exception ignored) {
            page.evaluate("window.scrollTo(0, document.body.scrollHeight / 2)");
        }

        Locator selects = xpath(XP_ROOM_QTY_SELECT);
        try {
            selects.first().waitFor(new Locator.WaitForOptions().setTimeout(8_000));
            return selects.count();
        } catch (Exception e) {
            return 0;
        }
    }

    public BookingPage clickFirstReserveButton() {
        try {
            Locator select = xpath(XP_ROOM_QTY_SELECT).first();
            select.waitFor(new Locator.WaitForOptions().setTimeout(5_000));
            select.selectOption("1");
        } catch (Exception ignored) {}

        Locator btn = xpathFirst(XP_RESERVE_SUBMIT);
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(8_000));
        } catch (Exception e) {
            return null;
        }
        try { btn.scrollIntoViewIfNeeded(); } catch (Exception ignored) {}

        try {
            Page bookPage = page.context().waitForPage(
                    new com.microsoft.playwright.BrowserContext.WaitForPageOptions().setTimeout(5_000),
                    () -> btn.click(new Locator.ClickOptions().setTimeout(5_000)));
            bookPage.waitForLoadState(
                    com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(20_000));
            return new BookingPage(bookPage);
        } catch (com.microsoft.playwright.TimeoutError ignored) {}

        try {
            page.waitForURL(url -> !url.contains("/hotel/"),
                    new Page.WaitForURLOptions().setTimeout(10_000));
        } catch (Exception ignored) {}

        page.waitForLoadState(
                com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                new Page.WaitForLoadStateOptions().setTimeout(15_000));
        return new BookingPage(page);
    }
}
