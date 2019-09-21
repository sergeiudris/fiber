# fiber: food app

## content

- [concept](#concept)
- [stages](#stages)
- [stage 1](#stage-1)

## concept


* fiber is a nutrients counter, food comparer
* features
    - search foods, see individual and aggregate values
    - select multiple food items for compare
    - query to answer 
        - what foods have glycimic between x and y
        - how much of x to meet y RDA
        - show foods highest in x but y,z... conditions
    - select different RDA sources
    - ? language support
    - 'google it' links (../?q=some query)

## stages

#### stage 1: mwp 

*read-only series: apps to explore data without storing

<div align="center">
<a>
<img width="80%" src="./resources/count1.png"></img>
</a>
</div>

* accelerate, make minimal working product
* users can:
    - search, select food items
    - see aggregate values

#### stage 2: design

* design minimal viable product

#### stage 3: mvp

* implement mvp

## stage 1

#### outline

* create a repo for data `fiber.data`
* link repo via symlink
* prepare .edn dataset
* make schema, load data
* try search
* add ui (pick a library other than antd)

* release via docker hub

#### resources

* usda datasets
    - https://github.com/awesomedata/awesome-public-datasets#agriculture

* 