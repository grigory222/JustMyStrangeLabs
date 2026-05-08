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
        // Таблица номеров грузится лениво — нужно проскроллить к ней.
        // Если якорь не найден (редкая верстка), скроллим в середину страницы.
        Locator anchor = xpathFirst(XP_AVAILABILITY_ANCHOR);
        if (anchor.count() > 0) {
            anchor.scrollIntoViewIfNeeded();
        } else {
            page.evaluate("window.scrollTo(0, document.body.scrollHeight / 2)");
        }

        Locator selects = xpath(XP_ROOM_QTY_SELECT);
        try {
            selects.first().waitFor(new Locator.WaitForOptions().setTimeout(8_000));
        } catch (com.microsoft.playwright.TimeoutError noRooms) {
            return 0;
        }
        return selects.count();
    }

    public BookingPage clickFirstReserveButton() {
        // Без выбора количества комнат форма уйдёт с qty=0 и booking покажет
        // ошибку "выберите комнату", не уйдя с /hotel/.
        Locator select = xpath(XP_ROOM_QTY_SELECT).first();
        if (select.count() > 0) {
            select.selectOption("1");
        }

        Locator btn = xpathFirst(XP_RESERVE_SUBMIT);
        try {
            btn.waitFor(new Locator.WaitForOptions().setTimeout(8_000));
        } catch (com.microsoft.playwright.TimeoutError notFound) {
            return null;
        }
        btn.scrollIntoViewIfNeeded();

        Page resultPage;
        try {
            resultPage = page.context().waitForPage(
                    new com.microsoft.playwright.BrowserContext.WaitForPageOptions().setTimeout(5_000),
                    () -> btn.click(new Locator.ClickOptions().setTimeout(5_000)));
        } catch (com.microsoft.playwright.TimeoutError noNewTab) {
            page.waitForURL(url -> !url.contains("/hotel/"),
                    new Page.WaitForURLOptions().setTimeout(10_000));
            resultPage = page;
        }

        resultPage.waitForLoadState(
                com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                new Page.WaitForLoadStateOptions().setTimeout(20_000));
        return new BookingPage(resultPage);
    }
}
