# Contributing to TorchLight

Thank you for your interest in contributing to TorchLight! This document provides guidelines to help you contribute effectively.

## Conventional Commits

We use the [Conventional Commits](https://www.conventionalcommits.org/) specification for commit messages. This format makes it easier to automatically generate changelogs and determine semantic version bumps.

### Commit Message Format

Each commit message consists of a **header**, an optional **body**, and an optional **footer**:

```
<type>(<optional scope>): <description>

<optional body>

<optional footer>
```

### Types

The commit type must be one of the following:

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (formatting, etc.)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing tests or correcting existing tests
- **build**: Changes that affect the build system or external dependencies
- **ci**: Changes to our CI configuration files and scripts
- **chore**: Other changes that don't modify src or test files

### How This Affects Versioning

The commit type determines how the version number will be updated:

- `fix` commits trigger a PATCH update (e.g., 1.0.0 → 1.0.1)
- `feat` commits trigger a MINOR update (e.g., 1.0.0 → 1.1.0)
- Commits with `BREAKING CHANGE` in the footer trigger a MAJOR update (e.g., 1.0.0 → 2.0.0)

### Examples

```
feat(commands): add distance command

Add a new command that allows adjusting the minimum distance threshold
for placing new light blocks. This helps reduce unnecessary block updates.

Closes #42
```

```
fix: prevent light blocks from replacing non-air blocks

The plugin was incorrectly replacing non-air blocks with light blocks when
a player moved. This fix ensures we only place light blocks in air spaces.

Fixes #123
```

```
feat!: require torch to be in offhand slot

BREAKING CHANGE: The plugin now requires torches to be in the offhand slot.
Previously, torches in either hand would work.
```

## Pull Request Process

1. Create a fork of the repository
2. Create a branch with a descriptive name (`fix/light-replacement-issue` or `feature/add-distance-command`)
3. Make your changes, following the code style of the project
4. Add or update tests as needed
5. Commit your changes using the conventional commit format
6. Push your branch to your fork
7. Open a pull request to the main repository

## Release Process

Our release process is automated using Release Please:

1. When commits are pushed to the main branch, a GitHub Action analyzes the conventional commits
2. If there are releasable changes, a Release PR is created or updated
3. When the Release PR is merged:
    - A new GitHub release is created with automatically generated release notes
    - The version in the POM file is updated
    - A JAR file is built and attached to the release

By following the conventional commit format, you're directly contributing to this automated process!
