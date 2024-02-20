# GoalPulse
A simple Android application for displaying statistics of major football competitions.

## Content
- [Technologies](#technologies)
- [Usage](#usage)

## Technologies
- [Realm](https://realm.io/)
- [Koin](https://insert-koin.io/)
- [Orbit MVI](https://orbit-mvi.org/)
- [Compose](https://developer.android.com/jetpack/compose)

## Usage
To use the application, obtain keys from the resources and replace them in the file ru.asmelnikov.utils.Constants:
- [Football API](https://www.football-data.org/client/register)
- [News API](https://newsapi.org/register)

```typescript
object Constants {

    const val FOOTBALL_API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    const val NEW_API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
}
```

## Structure
<img src=" [https://i.imgur.com/k4ZYqy7.png](https://github.com/MelnikovAleksandr/GoalPulse/assets/83123472/77459ff5-764e-482d-b4da-a7cc0bb136ad) " width="300">


