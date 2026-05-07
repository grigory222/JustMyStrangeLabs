package ru.itmo.tpo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void navigate(String url) {
        Page.NavigateOptions opts = new Page.NavigateOptions()
                .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                .setTimeout(45_000);
        try {
            page.navigate(url, opts);
        } catch (com.microsoft.playwright.TimeoutError first) {
            page.navigate(url, opts);
        }
    }

    protected Locator xpath(String expression) {
        return page.locator("xpath=" + expression);
    }

    protected Locator xpathFirst(String expression) {
        return page.locator("xpath=" + expression).first();
    }

    public String getTitle() {
        return page.title();
    }

    public String getUrl() {
        return page.url();
    }
}
