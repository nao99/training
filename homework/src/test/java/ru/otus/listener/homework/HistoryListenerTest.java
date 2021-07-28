package ru.otus.listener.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.history.MessagesHistory;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HistoryListenerTest {

    @Test
    void listenerTest() {
        // given
        var messagesHistory = MessagesHistory.of();
        var historyListener = new HistoryListener(messagesHistory);

        var id = 100L;
        var data = "33";

        var field13Data = new ArrayList<String>();
        field13Data.add(data);

        var field13 = new ObjectForMessage();
        field13.setData(field13Data);

        var message = new Message.Builder(id)
                .field10("field10")
                .field13(field13)
                .build();

        // when
        historyListener.onUpdated(message);
        message.getField13().setData(new ArrayList<>()); //меняем исходное сообщение
        field13Data.clear(); //меняем исходный список

        // then
        var messageFromHistory = historyListener.findMessageById(id);
        assertThat(messageFromHistory).isPresent();
        assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
    }

    @DisplayName("Should return the last added message from history")
    @Test
    void shouldFindLastMessageFromHistoryWhenFindById() throws Exception {
        // given
        var messagesHistory = MessagesHistory.of();
        var historyListener = new HistoryListener(messagesHistory);

        var message = new Message.Builder(1L).build();
        messagesHistory.push(message);

        // when
        var messageOptional = historyListener.findMessageById(message.getId());

        // then
        assertThat(messageOptional)
            .isPresent()
            .get()
            .isEqualTo(message);
    }

    @DisplayName("Should return an empty Optional when message was not found in the history")
    @Test
    void shouldReturnEmptyOptionalWhenFindById() throws Exception {
        // given
        var messagesHistory = MessagesHistory.of();
        var historyListener = new HistoryListener(messagesHistory);

        // when
        var messageOptional = historyListener.findMessageById(2L);

        // then
        assertThat(messageOptional)
            .isNotPresent();
    }
}
