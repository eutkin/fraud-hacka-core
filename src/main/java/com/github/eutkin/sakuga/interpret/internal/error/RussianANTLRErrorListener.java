package com.github.eutkin.sakuga.interpret.internal.error;

import com.github.eutkin.sakuga.interpret.exception.GrammaException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class RussianANTLRErrorListener implements ANTLRErrorListener {

    // Метод вызывается при обнаружении синтаксической ошибки
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // msg - это стандартное сообщение ANTLR, например,
        // "mismatched input 'x' expecting 'IF'"
        // "no viable alternative at input '...'"
        // "missing 'SEMICOLON' at '...'"
        // "extraneous input 'x' expecting 'SEMICOLON'"

        String errorMessage = "Синтаксическая ошибка";

        // Попробуем улучшить стандартное сообщение
        if (msg != null) {
            errorMessage = enhanceMessage(msg);
        }

        // Попытка получить текст символа, вызвавшего ошибку
        String offendingText = "<неизвестный символ>";
        if (offendingSymbol instanceof Token token) {
            if (token.getType() != Token.EOF) {
                offendingText = token.getText();
            } else {
                offendingText = "<конец файла>";
            }
        } else if (offendingSymbol != null) {
            offendingText = offendingSymbol.toString();
        }

        String message = String.format("Ошибка на строке %d:%d - %s. Проблемный символ: '%s'.%n",
                line, charPositionInLine, errorMessage, offendingText
        );
        if (recognizer instanceof Parser parser) {
            String ruleStack = parser.getRuleInvocationStack().toString();
            message += String.format("  Контекст (стек правил): %s%n", ruleStack);
        }

        throw new GrammaException(message, e);
    }

    // Метод для улучшения стандартного сообщения ANTLR
    private String enhanceMessage(String originalMsg) {
        // Примеры стандартных сообщений ANTLR и их возможная локализация:
        // "mismatched input 'x' expecting 'IF'"
        // "no viable alternative at input '...'"
        // "missing 'SEMICOLON' at '...'"
        // "extraneous input 'x' expecting 'SEMICOLON'"

        // Проверяем тип ошибки по сообщению
        if (originalMsg.startsWith("mismatched input")) {
            // Пример: "mismatched input 'x' expecting 'IF'"
            // Можно попытаться извлечь ожидаемый токен, но стандартное сообщение часто достаточно информативно.
            return "Несоответствие входных данных. " + originalMsg;
        } else if (originalMsg.startsWith("no viable alternative")) {
            // Пример: "no viable alternative at input 'func'"
            // Это означает, что ANTLR не может понять, как продолжить разбор.
            return "Не удалось распознать конструкцию. " + originalMsg;
        } else if (originalMsg.startsWith("missing")) {
            // Пример: "missing 'SEMICOLON' at '}'"
            // ANTLR уже формирует хорошее сообщение, просто добавим префикс.
            return "Отсутствует элемент: " + originalMsg;
        } else if (originalMsg.startsWith("extraneous input")) {
            // Пример: "extraneous input 'x' expecting 'SEMICOLON'"
            // Лишний символ, который мешает разбору.
            return "Лишний символ: " + originalMsg;
        } else if (originalMsg.startsWith("token recognition error")) {
            // Ошибка лексера, например, неизвестный символ.
            return "Ошибка распознавания токена (лексическая ошибка). " + originalMsg;
        } else if (originalMsg.startsWith("line")) {
            // Иногда ANTLR формирует сообщения вроде "line 5:10 token recognition error..."
            // или "line 5:10 mismatched input..."
            // Эти сообщения уже содержат локализацию, можно просто вернуть или слегка изменить.
            return "Ошибка разбора: " + originalMsg;
        }
        // Если неизвестное сообщение, возвращаем как есть или с общим префиксом
        return "Неизвестная синтаксическая ошибка: " + originalMsg;
    }


// --- Другие методы интерфейса, обычно не требуют сложной обработки для пользовательских сообщений ---

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        // Эти ошибки связаны с внутренними проблемами грамматики, обычно интересны разработчику грамматики
        // System.err.println("Обнаружена неоднозначность: " + ambigAlts);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        // Информация о том, что ANTLR пришлось использовать более сложный алгоритм разбора
        // System.err.println("Попытка полного контекста: " + conflictingAlts);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        // Информация о чувствительности к контексту
        // System.err.println("Чувствительность к контексту: " + prediction);
    }
}
