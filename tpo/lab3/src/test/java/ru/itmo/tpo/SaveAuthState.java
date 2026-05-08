package ru.itmo.tpo;

import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SaveAuthState {

    static final String STATE_FILE = "storage-state.json";

    public static void main(String[] args) throws Exception {
        System.out.println("=== SaveAuthState ===");
        System.out.println("Открываю браузер. Войдите в аккаунт booking.com,");
        System.out.println("затем нажмите Enter в этом терминале.");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(50)
                            .setArgs(List.of(
                                    "--disable-blink-features=AutomationControlled",
                                    "--disable-infobars",
                                    "--start-maximized")));

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(1280, 900)
                            .setLocale("en-US")
                            .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 "
                                    + "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"));

            Page page = context.newPage();
            page.addInitScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

            System.out.println("ВАЖНО: используйте вход через Email + пароль,");
            System.out.println("       НЕ через «Войти через Google» — Google блокирует автобраузеры.");
            page.navigate("https://account.booking.com/sign-in");

            System.out.println(">> Нажмите Enter после того, как увидите главную страницу с вашим аккаунтом...");
            System.in.read();

            Path out = Paths.get(STATE_FILE);
            context.storageState(new BrowserContext.StorageStateOptions().setPath(out));
            System.out.println("Состояние сохранено: " + out.toAbsolutePath());

            context.close();
            browser.close();
        }
    }
}
