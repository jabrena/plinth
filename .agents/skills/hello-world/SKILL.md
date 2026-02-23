---
name: hello-world
description: "Generates a working Hello World application in a user-chosen programming language. Asks the user to pick a language, then returns correct source code that prints \"Hello, World!\" to the console. Handles both compiled and interpreted languages correctly."
---

Ask the user which programming language they would like a Hello World application in. Offer a selection of common languages (e.g. Python, JavaScript, TypeScript, Rust, Go, C, C++, Java, C#, Ruby, Swift, Kotlin, Zig) and allow them to specify any other language.

Once the user has chosen a language:

1. Determine whether the language is **compiled** or **interpreted**.
2. Produce a complete, minimal source file that prints exactly `Hello, World!` to standard output (console).
3. The code must be correct and complete — it should compile (for compiled languages) and run without any errors on a standard toolchain with no extra dependencies.
4. Determine the conventional filename for a Hello World program in that language (e.g. `main.go`, `hello.py`, `Main.java`).
5. Write the source code to that file in the root of the current project using the Write tool (or equivalent file-writing tool).
6. Confirm the file was created and show the filename to the user.
7. After creating the file, provide a short "How to run" section that shows the exact shell commands needed to:
   - **Compiled languages**: compile the source file and then run the resulting binary.
   - **Interpreted languages**: run the source file directly with the appropriate interpreter/runtime.

Keep the code idiomatic and minimal — no unnecessary boilerplate beyond what the language requires for a valid runnable program.
