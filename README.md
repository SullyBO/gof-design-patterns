# Design Patterns Implementation

Personal study project implementing Gang of Four design patterns in Kotlin.

## Structure
```
patterns/
├── creational/
│   ├── abstract-factory/
│   ├── builder/
│   └── ...
├── structural/
└── behavioral/
```

## Development Setup
You'll need Gradle installed. On MacOS: `brew install gradle`

### Getting Started
```bash
git clone https://github.com/SullyBO/gof-design-patterns.git
cd gof-design-patterns

gradle wrapper
```

### Code Quality & Formatting
This project uses Spotless for formatting and Detekt for checking code quality:
```bash
# Check formatting and code quality
./gradlew spotlessCheck detekt             # Unix/MacOS: ./gradlew | Windows: gradlew

# Apply formatting fixes
./gradlew spotlessApply

# Build the project
./gradlew build

# Run quality, formatting, and build checks
./gradlew preCommitCheck
```

Before committing always run: `./gradlew preCommitCheck` or create a git hook (Optional):
```bash
echo '#!/bin/sh\n./gradlew preCommitCheck' > .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
```

### Creational
- ✅ Abstract Factory
- ✅ Builder
- ✅ Factory Method
- ✅ Prototype
- ✅ Singleton

### Structural
- [ ] Adapter
- [ ] Bridge
- [ ] Composite
- [ ] Decorator
- [ ] Facade
- [ ] Flyweight
- [ ] Proxy

### Behavioral
- [ ] Chain of Responsibility
- [ ] Command
- [ ] Interpreter
- [ ] Iterator
- [ ] Mediator
- [ ] Memento
- [ ] Observer
- [ ] State
- [ ] Strategy
- [ ] Template Method
- [ ] Visitor

## Language

**Kotlin**: Focus on idiomatic, Kotlin-native pattern implementations

## License

MIT
