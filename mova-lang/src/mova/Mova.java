package mova;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Mova {

    private static final Interpreter interpreter = new Interpreter();
    // Флаг, що вказує на наявність помилки
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    // Головний метод, який запускає інтерпретатор
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: mova [script]");
            System.exit(64);
        } else if (args.length == 1) {
            // Якщо задано один аргумент, запускаємо файл зі скриптом
            runFile(args[0]);
        } else {
            // Якщо аргументів немає, запускаємо інтерактивний режим
            runPrompt();
        }
    }

    // Метод для запуску скрипта з файлу
    private static void runFile(String path) throws IOException {
        // Зчитуємо всі байти з файлу за заданим шляхом
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // Перетворюємо байти в рядок та запускаємо обробку коду
        run(new String(bytes, Charset.defaultCharset()));
        // Якщо була помилка, завершуємо виконання з кодом помилки
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    // Метод для запуску інтерактивного режиму (REPL)
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        // Безкінечний цикл для читання рядків з консолі
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break; // якщо рядок порожній, виходимо з циклу
            run(line);               // обробляємо введений рядок
            hadError = false;        // скидаємо прапорець помилки після кожного рядка
        }
    }

    // Метод, який обробляє джерельний код: викликає Scanner та виводить токени
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        System.out.println("\n --- START TOKENIZATION ---\n");


        List<Token> tokens = scanner.scanTokens();

        // Зараз лише виводимо кожен токен для тестування
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\n --- END TOKENIZATION ---\n");

        System.out.println("\n --- START PARSING ---\n");
        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        System.out.println(new AstPrinter().print(expression));

        System.out.println("\n --- END PARSING ---\n");

        System.out.println("\n --- START EVALUATING ---\n");

        interpreter.interpret(expression);

        System.out.println("\n --- END EVALUATING ---\n");

    }

    // Метод для звіту про помилки, що приймає номер рядка та повідомлення
    static void error(int line, String message) {
        report(line, "", message);
    }

    // Допоміжний метод для виведення повідомлення про помилку у консоль
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }
}
