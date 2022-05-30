# About

Namaz app is an app that helps users keep track of their daily five prayers

# Architecture

- ViewModels + Usecases + Repositories
- Koin for dependency management

# Usecases

Main purpose for adding use cases on top of ViewModels is to prevent ViewModels from containing buisiness logic, which makes view models grow in size an complexity quickly

# Unit testing

Currently only Usecases are unit tested since they contain 100% of the business logic
