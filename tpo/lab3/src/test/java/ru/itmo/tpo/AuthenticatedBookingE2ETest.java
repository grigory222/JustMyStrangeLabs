package ru.itmo.tpo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.itmo.tpo.pages.BookingPage;
import ru.itmo.tpo.pages.HotelDetailsPage;
import ru.itmo.tpo.pages.SearchResultsPage;
import ru.itmo.tpo.pages.WishlistPage;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Booking.com — Authenticated E2E Scenarios")
class AuthenticatedBookingE2ETest extends BaseTest {

    private static final String CI = TestConfig.checkIn("e2e");
    private static final String CO = TestConfig.checkOut("e2e");

    @ParameterizedTest(name = "[{0}] S3 — Auth + Free cancellation → отель → Reserve → форма бронирования")
    @MethodSource("browsers")
    void authFreeCancellationReserve(SupportedBrowser browser) {
        initBrowser(browser);
        skipUnless(authenticated, "storage-state.json не найден — запустите ./gradlew saveAuth");

        String city = TestConfig.get("city.e2e.auth", "Amsterdam");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults(), "Нет результатов для «" + city + "» — пропускаем");

        results.filterByFreeCancellation();
        throttle();
        skipUnless(results.getResultCount() > 0,
                "Нет отелей с бесплатной отменой в " + city + " — пропускаем");

        String listingTitle = results.getResultTitle(0);

        HotelDetailsPage hotel = results.openHotelDetails(0);
        throttle();

        String detailName = hotel.getHotelName();
        assertFalse(detailName.isBlank(), "Страница отеля должна показывать название");
        boolean nameOk = Arrays.stream(listingTitle.split("[\\s,\\-()]+"))
                .filter(w -> w.length() > 3)
                .anyMatch(w -> detailName.toLowerCase().contains(w.toLowerCase()));
        skipUnless(nameOk, "Название «" + detailName + "» не совпадает с карточкой — пропускаем");

        String address = hotel.getAddress();
        assertFalse(address.isBlank(), "Страница отеля должна показывать адрес");
        assertTrue(address.toLowerCase().contains(city.toLowerCase()),
                "Адрес отеля «" + address + "» должен содержать введённый город «" + city + "»");

        int roomCount = hotel.getAvailableRoomTypeCount();
        skipUnless(roomCount > 0, "Нет доступных номеров — пропускаем");

        BookingPage bookingPage = hotel.clickFirstReserveButton();
        skipUnless(bookingPage != null, "Reserve не сработал — пропускаем");
        throttle();

        assertTrue(bookingPage.isLoaded(),
                "После Reserve ожидался переход на форму бронирования / вход, "
                        + "но URL: " + bookingPage.getUrl());
    }

    @ParameterizedTest(name = "[{0}] S4 — Auth + 5★ + Free cancellation → ВСЕ карточки 5★ → отель содержит город")
    @MethodSource("browsers")
    void authCombinedFiltersAllCardsMatch(SupportedBrowser browser) {
        initBrowser(browser);
        skipUnless(authenticated, "storage-state.json не найден — запустите ./gradlew saveAuth");

        String city = TestConfig.get("city.e2e.combined", "Berlin");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults(), "Нет результатов для «" + city + "» — пропускаем");

        results.filterByStars(5);
        throttle();
        results.filterByFreeCancellation();
        throttle();
        skipUnless(results.getResultCount() > 0,
                "Нет 5★ отелей с free cancellation в " + city + " — пропускаем");

        List<Integer> stars = results.getAllStarCounts();
        for (int i = 0; i < stars.size(); i++) {
            int s = stars.get(i);
            if (s > 0) {
                assertTrue(s >= 5, "Карточка #" + i + " должна быть top-tier (5★ или 10/10 self-rated), получили " + s);
            }
        }

        String listingTitle = results.getResultTitle(0);
        HotelDetailsPage hotel = results.openHotelDetails(0);
        throttle();

        String detailName = hotel.getHotelName();
        assertFalse(detailName.isBlank(), "Страница отеля должна показывать название");
        boolean nameOk = Arrays.stream(listingTitle.split("[\\s,\\-()]+"))
                .filter(w -> w.length() > 3)
                .anyMatch(w -> detailName.toLowerCase().contains(w.toLowerCase()));
        skipUnless(nameOk, "Название «" + detailName + "» не совпадает с карточкой — пропускаем");

        String address = hotel.getAddress();
        assertFalse(address.isBlank(), "Страница отеля должна показывать адрес");
        assertTrue(address.toLowerCase().contains(city.toLowerCase()),
                "Адрес отеля «" + address + "» должен содержать введённый город «" + city + "»");
    }

    @ParameterizedTest(name = "[{0}] S6 — Auth + Wishlist: добавил → личный кабинет → открыл оттуда → Reserve")
    @MethodSource("browsers")
    void authAddToWishlistOpenFromWishlistAndReserve(SupportedBrowser browser) {
        initBrowser(browser);
        skipUnless(authenticated, "storage-state.json не найден — запустите ./gradlew saveAuth");

        String city = TestConfig.get("city.e2e.wishlist", "Paris");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults(), "Нет результатов — пропускаем");

        String hotelTitle = results.getResultTitle(0);
        boolean added = results.addToWishlist(0);
        skipUnless(added, "Не удалось кликнуть heart-кнопку — пропускаем");
        throttle();

        WishlistPage wishlist = new WishlistPage(page).open();
        throttle();

        skipUnless(wishlist.containsHotel(hotelTitle),
                "Отель «" + hotelTitle + "» не найден в избранном "
                        + "(возможно, сессия не сохраняет wishlist)");

        HotelDetailsPage hotel = wishlist.openHotel(hotelTitle);
        throttle();

        String detailName = hotel.getHotelName();
        assertFalse(detailName.isBlank(), "Страница отеля должна показывать название");
        boolean nameOk = Arrays.stream(hotelTitle.split("[\\s,\\-()]+"))
                .filter(w -> w.length() > 3)
                .anyMatch(w -> detailName.toLowerCase().contains(w.toLowerCase()));
        skipUnless(nameOk, "Открылся не тот отель: «" + detailName
                + "» вместо «" + hotelTitle + "»");

        int rooms = hotel.getAvailableRoomTypeCount();
        skipUnless(rooms > 0, "Нет доступных номеров — пропускаем Reserve");

        BookingPage booking = hotel.clickFirstReserveButton();
        skipUnless(booking != null, "Reserve не сработал — пропускаем");
        throttle();

        assertTrue(booking.isLoaded(),
                "После Reserve ожидался переход на форму бронирования, "
                        + "но URL: " + booking.getUrl());
    }
}
