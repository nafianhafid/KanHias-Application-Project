# Kan Hias - Your Koi Fish Smart Marketplace

Bangkit Capstone Team ID : C22-PS290 <br>
Here is our repository for Bangkit 2022 Capstone project.

## Our Team

|              Name              |   Learning Path    | Bangkit ID |
| :----------------------------: | :----------------: | :--------: |
|     Lutfi Rachman Alfianto     |  Cloud Computing   | C2175G1729 |
|      Anugi Rako Tiyambodo      |  Cloud Computing   | C7464N3077 |
|  Muhammad Hafidh Ilmi Nafi'an  | Machine Learning   | M2006F0581 |
|    M. Ramadani Akbar Ariyadi   | Machine Learning   | M2378F2928 |
|           Faqih Raihan         | Machine Learning   | M2008G0854 |
|   Hafid Siraj Aurakhmah Witra  | Mobile Development | A7308F2635 |

## About Kan Hias Project

Kan Hias is Application of Prediction Koi Fish type and Marketplace for buying and selling koi fish

## Datasets

Link Datasets of this project : https://www.kaggle.com/datasets/nafianmuh/koi-dataset

## Deployment Link APK Kan Hias 

# Deep learning Koi FIsh classification

Kan Hias (especially in Koi Fish) classification using **convolutional neural network (CNN)** as feature extractor + softmax as classifier.

Our [dataset](https://www.kaggle.com/datasets/nafianmuh/koi-dataset) consists of 10 Koi Fish classes where each images will **belong to exactly one class**:

1. Asagi
1. Bekko
1. Goshiki
1. Hikarimono
1. Kohaku
1. Koromo
1. Sanke
1. Showa
1. Shusui
1. Utsurimono  

## Requirements

To run the application, you would need:

* CUDA device with global RAM >= 4 GB (tested with GTX 980)
* Python 2.7.x
* Virtualenv (optional. For isolated environment)

The programs expect batik image files (jpg/png) to be grouped by their classes. In this experiment, below directory structures are used:

```
train_data_dir/
	Asagi/*.jpg
	Bekko/*.jpg
	Goshiki/*.jpg
	Hikarimono/*.jpg
	Kohaku/*.jpg
  Koromo/*.jpg
  Sanke/*.jpg
  Showa/*.jpg
  Shusui/*.jpg
  Utsurimono/*.jpg

test_data_dir/
	Asagi/*.jpg
	Bekko/*.jpg
	Goshiki/*.jpg
	Hikarimono/*.jpg
	Kohaku/*.jpg
  Koromo/*.jpg
  Sanke/*.jpg
  Showa/*.jpg
  Shusui/*.jpg
  Utsurimono/*.jpg
```
