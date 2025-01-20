package mova;

enum TokenType {
    // Односимвольні токени.
    LEFT_PAREN,    // (
    RIGHT_PAREN,   // )
    LEFT_BRACE,    // {
    RIGHT_BRACE,   // }
    COMMA,         // ,
    DOT,           // .
    MINUS,         // -
    PLUS,          // +
    SEMICOLON,     // ;
    SLASH,         // /
    STAR,          // *

    // Токени з одним або двома символами.
    BANG,          // !
    BANG_EQUAL,    // !=
    EQUAL,         // =
    EQUAL_EQUAL,   // ==
    GREATER,       // >
    GREATER_EQUAL, // >=
    LESS,          // <
    LESS_EQUAL,    // <=

    // Літерали.
    IDENTIFIER,    // Ідентифікатори (імена змінних, функцій)
    STRING,        // Рядкові літерали
    NUMBER,        // Числові літерали

    // Ключові слова.
    AND,           // логічне "і"
    ELSE,          // "інакше"
    FALSE,         // false (хиба)
    FUN,           // "функція"
    FOR,           // "для"
    IF,            // "якщо"
    NIL,           // "ніщо"
    OR,            // логічне "або"
    PRINT,         // "вивести"
    RETURN,        // "повернути"
    TRUE,          // true (істина)
    VAR,           // "змінна"
    WHILE,         // "поки"

    EOF            // Кінець файлу
}
