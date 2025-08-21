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
Code Formatting
This project uses Spotless for consistent code formatting:

### Getting Starterd
```bash
git clone https://github.com/SullyBO/gof-design-patterns.git
cd design-patterns
```
```bash
# Check formatting
./gradlew spotlessCheck             # Unix/MacOS: ./gradlew | Windows: gradlew

# Apply formatting fixes
./gradlew spotlessApply

# Build the project
./gradlew build


# Run formatting checks
./gradlew preCommitCheck
```

Before committing, always run: `./gradlew preCommitCheck`

### Creational
- [ ] Abstract Factory
- [ ] Builder
- [ ] Factory Method
- [ ] Prototype
- [ ] Singleton

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

## Languages

**Kotlin**: Primary implementations focusing on idiomatic JVM patterns

## License

MIT
