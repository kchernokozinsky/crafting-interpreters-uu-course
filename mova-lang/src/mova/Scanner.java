package mova;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Клас Scanner відповідає за лексичний аналіз (розбиття вхідного тексту на токени)
class Scanner {
    // Карта ключових слів мови, де рядок — ключове слово, а TokenType — відповідний тип токену
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("і", TokenType.AND);
        keywords.put("інакше", TokenType.ELSE);
        keywords.put("хиба", TokenType.FALSE);
        keywords.put("для", TokenType.FOR);
        keywords.put("функція", TokenType.FUN);
        keywords.put("якщо", TokenType.IF);
        keywords.put("ніщо", TokenType.NIL);
        keywords.put("або", TokenType.OR);
        keywords.put("вивести", TokenType.PRINT);
        keywords.put("повернути", TokenType.RETURN);
        keywords.put("істина", TokenType.TRUE);
        keywords.put("змінна", TokenType.VAR);
        keywords.put("поки", TokenType.WHILE);
    }

    private final String source;         // Вхідний рядок з кодом
    private final List<Token> tokens = new ArrayList<>(); // Список розпізнаних токенів
    private int start = 0;               // Початкова позиція поточного лексемного блоку
    private int current = 0;             // Поточна позиція в рядку
    private int line = 1;                // Поточний номер рядка

    // Конструктор сканера, який приймає вихідний текст
    Scanner(String source) {
        this.source = source;
    }

    // Метод для сканування тексту і формування списку токенів
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // Встановлюємо початок нового лексемного блоку
            start = current;
            scanToken();
        }

        // Додаємо токен кінця файлу
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    // Метод перевіряє, чи досягнуто кінця вхідного тексту
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Метод сканує один символ чи групу символів і додає відповідний токен
    private void scanToken() {
        char c = advance(); // Отримуємо поточний символ і пересуваємо позицію
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    // Якщо це коментар, ігноруємо всі символи до кінця рядка.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Пропускаємо пробіли, символи повернення каретки та табуляції
                break;
            case '\n':
                // Перехід на новий рядок, збільшуємо лічильник рядків
                line++;
                break;
            case '"':
                // Обробка рядкового літералу
                string();
                break;
            default:
                // Якщо символ не співпадає з відомими, перевіряємо чи це цифра або літера
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Mova.error(line, "Unexpected character.");
                }
                break;
        }
    }

    // Метод обробляє ідентифікатори та ключові слова
    private void identifier() {
        // Продовжуємо, поки наступний символ є буквою або цифрою
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        // Перевіряємо, чи є розпізнаний текст ключовим словом
        TokenType type = keywords.get(text);
        if (type == null) type = TokenType.IDENTIFIER;
        addToken(type);
    }

    // Метод перевіряє, чи є символ літерою (латинська та українська кирилиця), або підкресленням
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c >= 'А' && c <= 'я') || // Основні кириличні літери
                c == 'Ґ' || c == 'ґ' ||   // Українські Ґ/ґ
                c == 'І' || c == 'і' ||   // Українські І/і
                c == 'Ї' || c == 'ї' ||   // Українські Ї/ї
                c == '_';
    }

    // Метод перевіряє, чи символ є літерою або цифрою
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // Метод перевіряє, чи символ є цифрою
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // Метод для сканування числового літералу
    private void number() {
        // Спершу обробляємо цілу частину
        while (isDigit(peek())) advance();

        // Перевіряємо, чи є дробова частина
        if (peek() == '.' && isDigit(peekNext())) {
            // Зчитуємо десяткову крапку
            advance();
            // Зчитуємо дробову частину
            while (isDigit(peek())) advance();
        }

        // Перетворюємо зчитаний рядок в число і додаємо токен
        addToken(TokenType.NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    // Метод повертає символ після поточної позиції без пересування
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    // Метод обробляє рядковий літерал
    private void string() {
        // Продовжуємо, поки не знайдемо закриваючі лапки або не досягнемо кінця
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        // Якщо досягли кінця без закриваючих лапок, повідомляємо про помилку
        if (isAtEnd()) {
            Mova.error(line, "Unterminated string.");
            return;
        }

        // Зчитуємо закриваючу лапку
        advance();

        // Вилучаємо рядок без лапок і додаємо токен
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    // Метод повертає поточний символ без пересування
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Метод перевіряє, чи наступний символ співпадає з очікуваним і, якщо так, пересуває позицію
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // Метод повертає поточний символ і пересуває позицію вперед
    private char advance() {
        return source.charAt(current++);
    }

    // Метод додає токен без літерального значення
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Метод створює токен з типом, зчитаним текстом та літеральним значенням і додає його до списку токенів
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
