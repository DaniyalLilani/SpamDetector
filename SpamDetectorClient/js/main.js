// TODO: onload function should retrieve the data needed to populate the UI

// JavaScript to toggle the light mode
document.getElementById('theme-toggle').addEventListener('click', function() {
  const themeIcon = this.querySelector('.material-symbols-outlined');
  document.body.classList.toggle('light-theme'); // Toggle the light theme class on the body

  // Check if the body now has the light-theme class
  if(document.body.classList.contains('light-theme')) {
    themeIcon.textContent = 'dark_mode'; // Change to dark mode icon
  } else {
    themeIcon.textContent = 'light_mode'; // Change to light mode icon
  }
});

function fetchData() {
  // Fetch and display the spam results
  fetch('http://localhost:8080/spamDetector-1.0/api/spam', {
    method: 'GET',
    headers: {
      Accept: 'application/json'
    }
  })
    .then(response => response.json())
    .then(data => {
      const tableBody = document.getElementById('chart').getElementsByTagName('tbody')[0];
      tableBody.innerHTML = ''; // Clear existing table content

      // Assuming data is an array of objects where each object has fileName, spamProbability, and actualClass properties
      data.forEach((item) => {
        let row = tableBody.insertRow();
        let fileNameCell = row.insertCell(0);
        let spamProbabilityCell = row.insertCell(1);
        let classCell = row.insertCell(2);


        fileNameCell.innerHTML = item.fileName;
        spamProbabilityCell.innerHTML = (item.spamProbability); // Grabs spamProbRounded
        classCell.innerHTML = item.actualClass; // Assuming 'actualClass' is the property name
      });
    })
    .catch(error => console.error('Error fetching data:', error));

}

function fetchAccuracy() {
  // Fetch and display the accuracy value
  fetch('http://localhost:8080/spamDetector-1.0/api/spam/accuracy', {
    method: 'GET',
    headers: {
      Accept: 'application/json'
    }
  })
    .then(response => response.json())
    .then(data => {
      // Assuming the returned data is just a number representing the accuracy
      document.getElementById('accuracy-value').textContent = `${data.toFixed(3)}%`;
    })
    .catch(error => console.error('Error fetching accuracy:', error));
}

function fetchPrecision() {
  // Fetch and display the accuracy value
  fetch('http://localhost:8080/spamDetector-1.0/api/spam/precision', {
    method: 'GET',
    headers: {
      Accept: 'application/json'
    }
  })
    .then(response => response.json())
    .then(data => {
      // Assuming the returned data is just a number representing the accuracy
      document.getElementById('precision-value').textContent = `${data.toFixed(3)}%`;
    })
    .catch(error => console.error('Error fetching accuracy:', error));
}

window.onload = function() {
  fetchData();
  fetchAccuracy();
  fetchPrecision();
};





