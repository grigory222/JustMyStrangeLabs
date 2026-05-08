package ru.itmo.tpo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class WishlistPage extends BasePage {

    private static final String URL = "https://www.booking.com/mywishlist.html";

    private static final String XP_HOTEL_LINKS =
            "//a[contains(@href, '/hotel/')]";

    public WishlistPage(Page page) {
        super(page);
    }

    public WishlistPage open() {
        navigate(URL);
        page.waitForURL(u -> u.contains("mywishlist"),
                new Page.WaitForURLOptions().setTimeout(10_000));
        return this;
    }

    public boolean containsHotel(String hotelName) {
        return findHotelLink(hotelName).count() > 0;
    }

    /**
     * Находит ссылку на отель по подстроке имени, кликает по ней,
     * возвращает страницу отеля. Booking может открыть отель в новой
     * вкладке или в текущей — обрабатываем оба варианта.
     */
    public HotelDetailsPage openHotel(String hotelName) {
        Locator link = findHotelLink(hotelName).first();
        link.waitFor(new Locator.WaitForOptions().setTimeout(5_000));
        link.scrollIntoViewIfNeeded();

        try {
            Page detailPage = page.context().waitForPage(
                    new com.microsoft.playwright.BrowserContext.WaitForPageOptions().setTimeout(5_000),
                    () -> link.click(new Locator.ClickOptions().setTimeout(5_000)));
            detailPage.waitForLoadState(
                    com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
            return new HotelDetailsPage(detailPage);
        } catch (com.microsoft.playwright.TimeoutError noNewTab) {
            page.waitForURL(u -> u.contains("/hotel/"),
                    new Page.WaitForURLOptions().setTimeout(10_000));
            return new HotelDetailsPage(page);
        }
    }

    private Locator findHotelLink(String hotelName) {
        String firstWord = firstSignificantWord(hotelName);
        return xpath(XP_HOTEL_LINKS + "[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                + firstWord.toLowerCase().replace("'", "&apos;") + "')]");
    }

    private static String firstSignificantWord(String s) {
        for (String w : s.split("[\\s,\\-()]+")) {
            if (w.length() > 3) return w;
        }
        return s;
    }
}
