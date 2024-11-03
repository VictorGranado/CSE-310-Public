import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy import stats


# Function to upload and read CSV or Excel file
def import_data(file_path):
    """
    Imports a dataset from a CSV or Excel file.
    Supports .csv and .xlsx files.
    """
    try:
        if file_path.endswith('.csv'):
            data = pd.read_csv(file_path)
        elif file_path.endswith('.xlsx'):
            data = pd.read_excel(file_path)
        else:
            raise ValueError("Unsupported file format. Use .csv or .xlsx.")
        print(f"Data imported successfully from {file_path}")
        return data
    except Exception as e:
        print(f"Error importing data: {e}")
        return None
    
    # Functions for basic statistical analysis
def calculate_statistics(data):
    """
    Calculates mean, median, mode, and standard deviation for numeric columns in the dataset.
    """
    stats = {}
    for column in data.select_dtypes(include=[np.number]).columns:
        stats[column] = {
            'mean': data[column].mean(),
            'median': data[column].median(),
            'mode': data[column].mode()[0],  # Get the first mode value
            'std_dev': data[column].std()
        }
    return stats

# Display statistics
def display_statistics(stats):
    """
    Prints the calculated statistics in a readable format.
    """
    for column, stat in stats.items():
        print(f"\nStatistics for {column}:")
        print(f"Mean: {stat['mean']}")
        print(f"Median: {stat['median']}")
        print(f"Mode: {stat['mode']}")
        print(f"Standard Deviation: {stat['std_dev']}")


# Handling missing values
def clean_missing_data(data, method='mean'):
    """
    Cleans missing data in the dataset. Supports filling with mean, median, or mode.
    """
    for column in data.columns:
        if data[column].isnull().sum() > 0:
            if method == 'mean':
                data[column].fillna(data[column].mean(), inplace=True)
            elif method == 'median':
                data[column].fillna(data[column].median(), inplace=True)
            elif method == 'mode':
                data[column].fillna(data[column].mode()[0], inplace=True)
    print("Missing data handled successfully.")
    return data

# Handling outliers using Z-score
def remove_outliers(data, threshold=3):
    """
    Removes outliers from numeric columns using the Z-score method.
    Outliers are data points where the absolute Z-score is greater than the threshold.
    """
    z_scores = np.abs(stats.zscore(data.select_dtypes(include=[np.number])))
    data = data[(z_scores < threshold).all(axis=1)]
    print(f"Outliers removed successfully. {len(data)} rows remain.")
    return data


# Data visualization functions
def plot_histogram(data, column):
    """
    Plots a histogram for a specified numeric column.
    """
    plt.figure(figsize=(8, 6))
    plt.hist(data[column], bins=10, color='skyblue')
    plt.title(f'Histogram of {column}')
    plt.xlabel(column)
    plt.ylabel('Frequency')
    plt.show()

def plot_scatter(data, column1, column2):
    """
    Plots a scatter plot between two numeric columns.
    """
    plt.figure(figsize=(8, 6))
    plt.scatter(data[column1], data[column2], color='purple')
    plt.title(f'Scatter plot of {column1} vs {column2}')
    plt.xlabel(column1)
    plt.ylabel(column2)
    plt.show()


# Exporting cleaned and analyzed data
def export_data(data, output_file):
    """
    Exports the cleaned and analyzed data to a specified output file (CSV or Excel).
    """
    try:
        if output_file.endswith('.csv'):
            data.to_csv(output_file, index=False)
        elif output_file.endswith('.xlsx'):
            data.to_excel(output_file, index=False)
        else:
            raise ValueError("Unsupported file format. Use .csv or .xlsx.")
        print(f"Data exported successfully to {output_file}")
    except Exception as e:
        print(f"Error exporting data: {e}")


def main():
    # Step 1: Import Data
    file_path = input("Enter the file path of the dataset (.csv or .xlsx): ")
    data = import_data(file_path)
    
    if data is not None:
        # Step 2: Data Cleaning
        data = clean_missing_data(data, method='mean')
        data = remove_outliers(data)

        # Step 3: Perform Statistical Analysis
        stats = calculate_statistics(data)
        display_statistics(stats)

        # Step 4: Data Visualization
        column = input("Enter the column for histogram plotting: ")
        plot_histogram(data, column)
        
        column1 = input("Enter the first column for scatter plot: ")
        column2 = input("Enter the second column for scatter plot: ")
        plot_scatter(data, column1, column2)

        # Step 5: Export the results
        output_file = input("Enter the output file path for saving results (.csv or .xlsx): ")
        export_data(data, output_file)
    else:
        print("No data to process.")
    
if __name__ == "__main__":
    main()
