package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[placeholder=Город]").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();

        $x("//*[@data-test-id='success-notification']//*[@class='notification__content']")
                .shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));
        $x("//*[@data-test-id='success-notification']//button").click();

        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $x("//*[contains(text(), 'Запланировать')]").click();

        $x("//*[@data-test-id='replan-notification']//*[@class='notification__content']")
                .shouldBe(visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//*[@data-test-id='replan-notification']//button[not(contains(@class, 'notification__closer'))]").click();

        $x("//*[@data-test-id='success-notification']//*[@class='notification__content']")
                .shouldBe(visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
        $x("//*[@data-test-id='success-notification']//button").click();
    }
}
