package ru.itmo.tpo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchResultsPage extends BasePage {

    private static final String XP_PROPERTY_CARDS =
            "//div[@data-testid='property-card']";

    private static final String XP_CARD_TITLE =
            ".//div[@data-testid='title']";

    private static final String XP_TITLE_GLOBAL =
            "//div[@data-testid='property-card']//div[@data-testid='title']";

    private static final String XP_TITLE_LINK =
            ".//a[@data-testid='title-link']";

    private static final String XP_PRICE =
            ".//*[@data-testid='price-and-discounted-price']";

    private static final String XP_RATING_STARS =
            ".//div[@data-testid='rating-stars']";

    private static final String XP_REVIEW_SCORE =
            ".//*[@data-testid='review-score']";

    private static final String XP_ADDRESS =
            ".//*[@data-testid='address' or @data-testid='address-link']";

    private static final String XP_SORT_DROPDOWN_TRIGGER =
            "//button[@data-testid='sorters-dropdown-trigger']";

    private static final String XP_SORT_OPTION_FMT =
            "//div[@data-testid='sorters-dropdown']"
            + "//button[@role='option' and normalize-space(.)='%s']";

    private static final String XP_STAR_FILTER_FMT =
            "//input[@type='checkbox' and @name='class=%d']";

    private static final String XP_FREE_CANCEL_FILTER =
            "//input[@type='checkbox' and @name='fc=2']";

    public SearchResultsPage(Page page) {
        super(page);
    }

    public boolean hasResults() {
        try {
            xpathFirst(XP_TITLE_GLOBAL)
                    .waitFor(new Locator.WaitForOptions().setTimeout(15_000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        try {
            xpathFirst(XP_TITLE_GLOBAL)
                    .waitFor(new Locator.WaitForOptions().setTimeout(15_000));
        } catch (Exception e) {
            return 0;
        }
        return xpath(XP_TITLE_GLOBAL).count();
    }

    public String getResultTitle(int index) {
        return xpath(XP_TITLE_GLOBAL).nth(index).textContent().trim();
    }

    public long getResultPrice(int index) {
        try {
            String text = xpath(XP_PROPERTY_CARDS).nth(index)
                    .locator("xpath=" + XP_PRICE)
                    .first()
                    .textContent();
            String digits = text.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? Long.MAX_VALUE : Long.parseLong(digits);
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }

    public int getResultStarCount(int index) {
        try {
            Locator stars = xpath(XP_PROPERTY_CARDS).nth(index)
                    .locator("xpath=" + XP_RATING_STARS);
            if (stars.count() == 0) return -1;
            String label = stars.first().getAttribute("aria-label");
            if (label != null) {
                java.util.regex.Matcher m =
                        java.util.regex.Pattern.compile("(\\d+)").matcher(label);
                if (m.find()) return Integer.parseInt(m.group(1));
            }
            int svg = stars.locator("svg").count();
            return svg > 0 ? svg : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public double getResultReviewScore(int index) {
        try {
            Locator card = xpath(XP_PROPERTY_CARDS).nth(index);
            Locator el = card.locator("xpath=" + XP_REVIEW_SCORE);
            if (el.count() == 0) return Double.NaN;
            String text = el.first().textContent();
            java.util.regex.Matcher m =
                    java.util.regex.Pattern.compile("(\\d+[.,]\\d+)").matcher(text);
            if (m.find()) return Double.parseDouble(m.group(1).replace(",", "."));
            return Double.NaN;
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public java.util.List<Integer> getAllStarCounts() {
        int n = xpath(XP_PROPERTY_CARDS).count();
        java.util.List<Integer> out = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(getResultStarCount(i));
        return out;
    }

    public java.util.List<Long> getAllPrices() {
        int n = xpath(XP_PROPERTY_CARDS).count();
        java.util.List<Long> out = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(getResultPrice(i));
        return out;
    }

    public java.util.List<Double> getAllReviewScores() {
        int n = xpath(XP_PROPERTY_CARDS).count();
        java.util.List<Double> out = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(getResultReviewScore(i));
        return out;
    }

    public String getResultAddress(int index) {
        try {
            Locator card = xpath(XP_PROPERTY_CARDS).nth(index);
            Locator el = card.locator("xpath=" + XP_ADDRESS);
            if (el.count() == 0) return "";
            return el.first().textContent().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public java.util.List<String> getAllAddresses() {
        int n = xpath(XP_PROPERTY_CARDS).count();
        java.util.List<String> out = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(getResultAddress(i));
        return out;
    }

    public SearchResultsPage filterByStars(int stars) {
        page.keyboard().press("Escape");
        Locator checkbox = xpathFirst(String.format(XP_STAR_FILTER_FMT, stars));
        checkbox.waitFor();
        checkbox.click();
        waitForListReload();
        return this;
    }

    public SearchResultsPage filterByFreeCancellation() {
        page.keyboard().press("Escape");
        Locator checkbox = xpathFirst(XP_FREE_CANCEL_FILTER);
        checkbox.waitFor();
        checkbox.click();
        waitForListReload();
        return this;
    }

    public SearchResultsPage sortBy(String optionText) {
        page.keyboard().press("Escape");
        xpathFirst(XP_SORT_DROPDOWN_TRIGGER).click();
        Locator option = xpathFirst(String.format(XP_SORT_OPTION_FMT, optionText));
        option.waitFor();
        option.click();
        waitForListReload();
        return this;
    }

    private void waitForListReload() {
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.LOAD,
                new Page.WaitForLoadStateOptions().setTimeout(15_000));
        xpathFirst(XP_TITLE_GLOBAL)
                .waitFor(new Locator.WaitForOptions().setTimeout(10_000));
    }

    public HotelDetailsPage openHotelDetails(int index) {
        Locator card = xpath(XP_PROPERTY_CARDS).nth(index);
        Locator candidate = card.locator("xpath=" + XP_TITLE_LINK).first();
        if (candidate.count() == 0) {
            candidate = card.locator("xpath=" + XP_CARD_TITLE).first();
        }
        final Locator titleLink = candidate;
        titleLink.waitFor();

        try {
            Page detailPage = page.context().waitForPage(
                    new com.microsoft.playwright.BrowserContext.WaitForPageOptions().setTimeout(8_000),
                    () -> titleLink.click());
            detailPage.waitForLoadState(
                    com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
            return new HotelDetailsPage(detailPage);
        } catch (com.microsoft.playwright.TimeoutError e) {
            page.waitForLoadState(
                    com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(15_000));
            return new HotelDetailsPage(page);
        }
    }
}
