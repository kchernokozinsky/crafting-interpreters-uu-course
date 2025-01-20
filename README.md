# Реалізація лексичного аналізатора для Mova

Ця гілка містить реалізацію лексичного аналізатора (сканера) для мови **Mova** — проєкту університетського курсу з розробки інтерпретаторів. Проєкт базується на ідеях з ресурсу [Crafting Interpreters](https://craftinginterpreters.com/).

## Що зроблено

- **Розбиття тексту на токени:**  
  Реалізовано основний механізм сканування вхідного коду, який зчитує сирий текст та розбиває його на окремі токени. Це включає обробку односимвольних токенів (дужок, операторів, ком, крапок тощо).

- **Підтримка числових та рядкових літералів:**  
  Реалізовано методи для розпізнавання чисел (цілих та дробових) і рядкових літералів, де рядкові значення знаходяться в лапках.

- **Обробка ідентифікаторів та ключових слів:**  
  Розроблено логіку для розрізнення ідентифікаторів (імен змінних, функцій) і ключових слів мови. Ключові слова реалізовано українською (наприклад, `якщо`, `і`, `або`, `змінна` тощо).

- **Підтримка латинських та українських символів:**  
  Функції для перевірки символів були розширені для роботи як з латинськими, так і з українськими (кирилиця) літерами.

## Як запустити

1. **Встановіть необхідні інструменти:**  
   Переконайтеся, що встановлені Java, Git та середовище розробки (рекомендовано [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)).

2. **Клонуйте репозиторій:**

   ```bash
   git clone https://github.com/kchernokozinsky/crafting-interpreters-uu-course.git

3. **Перейдіть на гілку** `feature/lexer`

   ```bash
   git checkout feature/lexer

4. **Запустіть проєкт** `feature/lexer`

Відкрийте проєкт у вашому IDE та запустіть клас Mova, який містить інтерактивний режим та підтримку виконання скриптів.

## Корисні посилання

[Lexer/Scanner](https://craftinginterpreters.com/scanning.html#the-scanner-class) - розділ про лексичний аналіз
