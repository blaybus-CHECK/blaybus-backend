package com.blaybus.demo;

import com.blaybus.demo.command.RegisterMainWorkCommand;
import com.blaybus.demo.view.MainWorkView;
import org.assertj.core.api.ThrowingConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class MainWorkAssertion {
    public static ThrowingConsumer<? super MainWorkView> isDerivedForm(RegisterMainWorkCommand command) {
        return mainWork -> {
            assertThat(mainWork.title()).isEqualTo(command.title());
            assertThat(mainWork.description()).isEqualTo(command.description());
        };
    }
}
