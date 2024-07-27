# Spam Detector Dashboard

## Project Overview

This Spam Detector is designed to filter out spam emails, improving the user's email experience. It uses a unigram approach to analyze email content and predict whether an email is spam. The backend logic is implemented in Java, using RESTful services to communicate with the frontend, which displays results through a dynamic web interface.

## Features

- **Spam Detection Algorithm**: Utilizes a naive Bayes classifier to determine the probability that an incoming email is spam.
- **RESTful API**: Exposes endpoints for fetching spam detection results, accuracy, and precision metrics.
- **Interactive Web Dashboard**: A user-friendly interface for visualizing spam detection results and model performance metrics, including accuracy and precision.
- **Theme Toggle**: Offers light and dark mode for user preference.

## Technologies Used

- Backend: Java, Jackarta (for RESTful services)
- Frontend: HTML, CSS, JavaScript
- Libraries/APIs: Google Fonts for icons, Fetch API for AJAX requests

## Getting Started

### Prerequisites

- JDK 11 or later
- Any modern web browser
- GlassFish Server (version 7.0.9 or later)

## Server Deployment with GlassFish 7.0.9+

### Requirements
- GlassFish Server 7.0.9 or later
- JDK 11 or newer (as required by GlassFish 7.0.9+)

### Setting Up GlassFish
1. Download GlassFish 7.0.9 or later from the [GlassFish Releases Page](https://github.com/eclipse-ee4j/glassfish/releases).
2. Install JDK 11 or newer, if not already installed. Verify the installation by running `java -version` in your terminal.
3. Extract the GlassFish zip file to a directory of your choice.
4. Navigate to the GlassFish directory in a terminal or command prompt.

### Running the Application

1. **Start the Backend Server**:
   - Navigate to the backend project directory.
   - Compile and run the Java application.

2. **Accessing the Web Interface**:
   - Open the `index.html` file in a web browser.
   - Interact with the dashboard to view spam detection results and performance metrics.

## The Frontend
We refined the frontend by updating the color palette for a more attractive look, added light and dark mode toggles for user convenience, and introduced a dashboard logo for a professional touch. An about section was also incorporated to credit contributors. These enhancements aim to make the UI visually appealing, more user-friendly, and easy to navigate.
### Here's how it looks

### Dark mode
![Dashboard Dark Mode](https://github.com/OntarioTech-CS-program/w24-csci2020u-assignment01-lilani-pianezza-liang-khashefi-aazam/blob/main/Images/Dark.png)

### Light Mode
![Dashboard Light Mode](https://github.com/OntarioTech-CS-program/w24-csci2020u-assignment01-lilani-pianezza-liang-khashefi-aazam/blob/main/Images/Light.png)

### About page
![About Page](https://github.com/OntarioTech-CS-program/w24-csci2020u-assignment01-lilani-pianezza-liang-khashefi-aazam/blob/main/Images/About.png)


## How It Works

1. **Training the Model**: The backend processes a dataset of emails to train the spam detection model, calculating word frequencies and probabilities.
2. **Testing and Evaluation**: The model is then tested with a separate dataset, evaluating its accuracy and precision.
3. **Displaying Results**: The frontend fetches results from the backend via RESTful API calls and displays them on the dashboard.


## Improvements to the algorithm.
For any instances where `Pr(S|W_i) == 1` or `Pr(S|W_i) == 0`, we did not add that word to our list of spam probabilities. We did this to avoid having the sum expression equal infinity. This drastically improved our precision and accuracy into the low 90's range (%)
If we had time, we would have refined our logic to match laplace smoothing in naive bayes algorithm. Perhaps we will refine it in the future as a personal project.


## Contributors

- Daniyal Lilani - https://github.com/DaniyalLilani
- Aryan Kashefi-Azam - https://github.com/Aryan-KA
- Jilun Liang - https://github.com/jilunliang
- Robert Pianezza - https://github.com/MagneticZebra

## Acknowledgments

- https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering
- https://en.wikipedia.org/wiki/Bag-of-words_model

