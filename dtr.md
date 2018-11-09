# Blocks DTR

## Goals

### Seamus
  * Improve on FP paradigms
    * explore flexibility and dynamism
  * Build something together (w/ Mike)
  * Specialties: 

### Mike
  * Fully functional project
  * Work w/ Seamus
  * Potentially get users
  * Extensible
  * Testing + code quality
  * Maybe machine learning
  * 

## Stack Potential

|Back|Front|
|---|---|
|Elixir|Elm|
|**Clojure**|**ClojureScript**|
|~~Haskell~~|~~Purescript~~|

* Elixir: dynamic (dialyzer), Ruby + erlang "fault tolerant"

```elixir
Enum.map(fn x -> x + 1 end, [1,2,3])
```

* Elm: JS -> static -> compiles -> source of redux pattern

```elm
inc :: Int -> Int
inc x = x + 1
List.map inc [1,2,3]
```

* Clojure: dynamic (spec)

* Rich Hickey "programming is about thinking, not about typing"

```clj
(map (fn [x] (+ x 1)) [1 2 3])
(map #(+ %1 1) [1 2 3])
(reduce (fn [acc el]))
```

## Tools

* Task management: github issues
* Code formatters: TBD
* CD/CI travis CI
* Heroku/Firebase/AWS
* React Native maybe?
* PWA

## Themes

Journeying
roads
blocks
building

## Schema

* User
  * has many paths
* Guide
  * belongs to user
  * has many users
* Path: frequency, activity, duration, completed
  * has one active path
  * has many users
  * has many checkpoints
  * has many iterations
* Path Queue
* Pebble: unit of frequency, when a user submits data
  * belongs to path
* Checkpoints
  * Belongs to path

## Features

Data visualizations of current and old paths

Potential pebble dashboard

```
| * | * | * | * |   |   |   |   |   |   |
| * | * | * | * | * | * | * | * | * | * |
| * | * | * | * | * | * | * | * | * | * |
| * | * | * | * | * | * | * | * | * | * |
| * | * | * | * | * | * | * | * | * | * |
| * | * | * | * | * | * | * | * | * | * |
```

## Components

* Calendar

## First Stories

### Auth

* User creates account
* User logs in
* User logs out
* User edits profile
* Delete account

### Path

As a logged in user
I can create a path
  * title
  * motivation
  * duration (e.g., 30 days)
  * time of day (optional) (Morning/Afternoon/Evening)

#### MVP = wake up at 6 am for 30 days

## Next steps (11/8/18)

* Git repos
  * Seamus client
  * Mike server
* Cards
  * Seamus designs and front-end story
