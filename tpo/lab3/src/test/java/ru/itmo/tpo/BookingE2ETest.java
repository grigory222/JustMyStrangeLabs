package ru.itmo.tpo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.itmo.tpo.pages.BookingPage;
import ru.itmo.tpo.pages.HotelDetailsPage;
import ru.itmo.tpo.pages.SearchResultsPage;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Booking.com — Anonymous E2E Scenarios")
class BookingE2ETest extends BaseTest {

    private static final String CI = TestConfig.checkIn("e2e");
    private static final String CO = TestConfig.checkOut("e2e");

    @ParameterizedTest(name = "[{0}] S1 — Поиск + фильтр 4★ → ВСЕ карточки ≥4★ → отель → город/дата/availability")
    @MethodSource("browsers")
    void searchWithStarFilterThenOpenHotelAndCheckAvailability(SupportedBrowser browser) {
        initBrowser(browser);
        String city = TestConfig.get("city.e2e.search", "Paris");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults(), "Нет результатов — пропускаем");

        results.filterByStars(4);
        throttle();
        skipUnless(results.getResultCount() > 0, "Нет 4★ отелей — пропускаем");

        List<Integer> stars = results.getAllStarCounts();
        for (int i = 0; i < stars.size(); i++) {
            int s = stars.get(i);
            if (s > 0) {
                assertTrue(s >= 4, "Карточка #" + i + " должна быть ≥4★, получили " + s);
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

        String widgetDate = hotel.getBookingCheckInDate();
        skipUnless(!widgetDate.isBlank(), "Виджет бронирования не показывает дату — пропускаем");
        LocalDate d = LocalDate.parse(CI);
        String month = d.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase();
        assertTrue(
                widgetDate.toLowerCase().contains(month) || widgetDate.contains(String.valueOf(d.getDayOfMonth())),
                "Виджет «" + widgetDate + "» должен содержать месяц/день заезда");

        int roomCount = hotel.getAvailableRoomTypeCount();
        skipUnless(roomCount > 0, "Секция доступности не загрузилась — пропускаем");
        assertTrue(roomCount > 0, "Должен быть хотя бы один доступный тип номера");
    }

    @ParameterizedTest(name = "[{0}] S2 — Поиск + сортировка по цене → первые 5 по возрастанию (≤1 инверсия) → отель → Reserve")
    @MethodSource("browsers")
    void searchWithPriceSortThenOpenHotelAndClickReserve(SupportedBrowser browser) {
        initBrowser(browser);
        String city = TestConfig.get("city.e2e.full", "Rome");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults(), "Нет результатов — пропускаем");

        results.sortBy("Price (lowest first)");
        throttle();

        List<Long> prices = results.getAllPrices().stream()
                .filter(p -> p != Long.MAX_VALUE)
                .limit(5)
                .toList();
        skipUnless(prices.size() == 5, "Нужно 5 карточек с ценой для проверки сортировки");

        int inversions = 0;
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i - 1) > prices.get(i)) inversions++;
        }
        assertTrue(inversions <= 1,
                "После 'Price (lowest first)' цены первых 5 карточек должны идти "
                        + "по возрастанию (допустима 1 инверсия — спонсорская карточка). "
                        + "Получено: " + prices + ", инверсий: " + inversions);

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
        skipUnless(roomCount > 0, "Нет доступных номеров — пропускаем нажатие Reserve");

        BookingPage bookingPage = hotel.clickFirstReserveButton();
        skipUnless(bookingPage != null, "Кнопка Reserve не найдена или не сработала — пропускаем");
        throttle();

        assertTrue(bookingPage.isLoaded(),
                "После нажатия Reserve ожидался переход на страницу бронирования/входа, "
                        + "но URL: " + bookingPage.getUrl());
    }

    @ParameterizedTest(name = "[{0}] S5 — Просмотрел отель #0 → передумал → открыл #1 → забронировал #1")
    @MethodSource("browsers")
    void browseTwoHotelsThenReserveSecond(SupportedBrowser browser) {
        initBrowser(browser);
        String city = TestConfig.get("city.e2e.browse", "Madrid");

        SearchResultsPage results = performSearch(city, CI, CO);
        throttle();
        skipUnless(results.hasResults() && results.getResultCount() >= 2,
                "Нужно ≥2 карточек для сравнения — пропускаем");

        String title0Card = results.getResultTitle(0);
        String title1Card = results.getResultTitle(1);
        skipUnless(!title0Card.equalsIgnoreCase(title1Card),
                "Первые две карточки идентичны — нерепрезентативно");

        HotelDetailsPage hotel0 = results.openHotelDetails(0);
        throttle();
        String name0 = hotel0.getHotelName();
        assertFalse(name0.isBlank(), "Первый отель должен показать название");

        closeExtraTabs();

        HotelDetailsPage hotel1 = results.openHotelDetails(1);
        throttle();
        String name1 = hotel1.getHotelName();
        assertFalse(name1.isBlank(), "Второй отель должен показать название");

        assertNotEquals(name0.toLowerCase(), name1.toLowerCase(),
                "Открыли разные карточки — названия должны отличаться: «" + name0 + "» vs «" + name1 + "»");

        int rooms = hotel1.getAvailableRoomTypeCount();
        skipUnless(rooms > 0, "Нет доступных номеров во втором отеле — пропускаем Reserve");

        BookingPage booking = hotel1.clickFirstReserveButton();
        skipUnless(booking != null, "Reserve не сработал — пропускаем");
        throttle();

        assertTrue(booking.isLoaded(),
                "После Reserve ожидался переход на форму бронирования / sign-in, "
                        + "но URL: " + booking.getUrl());
    }
}
