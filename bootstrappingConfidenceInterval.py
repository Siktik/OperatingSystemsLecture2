import pandas as pd
import numpy as np

file_path = "data/InterProcessDockerizedCombined.csv"  # Replace with your actual file path
np.random.seed(42)
# Read the CSV and store values in a 1D numpy array
values = np.genfromtxt(file_path, delimiter=';', skip_header=0)

# Remove NaN values from the array
values_clean = values[~np.isnan(values)]

# Print the cleaned array
print(values_clean)

# Bootstrap parameters
n_iterations = 10000  # Number of bootstrap resamples
confidence_level = 0.95

# Resample with replacement and calculate means
bootstrap_means = []
n = len(values_clean)

for _ in range(n_iterations):
    # Resample with replacement
    sample = np.random.choice(values_clean, size=n, replace=True)
    # Calculate the mean of the resampled data
    bootstrap_means.append(np.mean(sample))

# Calculate the lower and upper percentiles for the 95% CI
lower_percentile = (1 - confidence_level) / 2 * 100
upper_percentile = (1 + confidence_level) / 2 * 100

lower_bound = np.percentile(bootstrap_means, lower_percentile)
upper_bound = np.percentile(bootstrap_means, upper_percentile)

# Print results
print(f"95% Confidence Interval using Bootstrapping: ({lower_bound:.2f}, {upper_bound:.2f})")
