# Kustom

Kustom is an interpreted language, written in Kotlin native. Kustom is design with ease-of-use in mind and imposes
minimal syntax restrictions on users.

## Key Features

#### Everything is an expression!
In Kustom, every statement is an expression, meaning the syntax is very fluid and intuitive.

#### Turning Complete
You can do everything in Kustom that you can do in Java, Node, Kotlin (etc)

#### Multiplatform
Kustom can be ran on any platform where the interpreter is present.

#### Untyped
Types are restrictive! Kustom has no strict typing 

## Syntax

```javascript
/* Ask the user for their name */
name = read("What is your name?")

// If they're called Kustom, they're awesome!
if(name == "Kustom") {
    println("You are awesome!")
}
```
## Usage

Build the executable:

```bash
./gradlew assemble
```

The executable can be found in `build/bin/native/releaseExecutable`

Execute:

```bash
kustom-native <file>
```

- **file** - Optional file path to be executed. If not provided, will begin
interactive REPL.

## The future of Kustom

- Full OO and functional support
- Compilation to JVM Byecode
- Compilation to native
