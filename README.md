# BusterX

Root-required Android gaming booster foundation.

## What it does

- Verifies real `su`/UID 0 access.
- Provides a lightweight Compose dashboard.
- Tracks application PSS memory.
- Starts/stops a gaming-session state.
- Includes GitHub Actions APK build.
- Uses no paid API, account, backend, or subscription.

## Safety boundary

BusterX does not modify game memory, inject code, alter hitboxes, automate aiming, or bypass anti-cheat systems.

## Build

The repository is intended to build with GitHub Actions. The workflow produces a debug APK artifact.

## Root

A rooted Android device with a functioning `su` provider (such as Magisk) is required for the application to enter its rooted operating mode.
