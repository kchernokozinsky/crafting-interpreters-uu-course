package mova;

// Клас Token представляє окремий токен (лексему) у вхідному коді.
class Token {
    // Тип токена (наприклад, ключове слово, оператор, літерал тощо).
    final TokenType type;
    // Рядок, який відповідає зчитаній лексемі.
    final String lexeme;
    // Літеральне значення, якщо воно є (наприклад, числове чи рядкове значення).
    final Object literal;
    // Номер рядка, на якому зустрічається токен.
    final int line;

    // Конструктор, що ініціалізує всі поля токена.
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    // Метод toString повертає текстове представлення токена.
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
