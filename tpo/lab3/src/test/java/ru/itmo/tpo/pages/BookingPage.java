package ru.itmo.tpo.pages;

import com.microsoft.playwright.Page;

public class BookingPage extends BasePage {

    public BookingPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        try {
            page.waitForLoadState(
                    com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
        } catch (Exception ignored) {}

        return page.url().contains("secure.booking.com/");
    }
}
