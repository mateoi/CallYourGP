# -*- coding: utf-8 -*-
"""
Displays color-coded tables of Pong results
Created on Tue Jan 31 16:36:27 2017

@author: mateo
"""
import re
import numpy as np
from matplotlib import pyplot as plt

FILENAME = "D:/Documents/workspace/CallYourGP/Pong_Results.txt"


def __main__():
    lines = open_lines(FILENAME)
    all_tables = np.array([line_to_table(line) for line in lines])
    matrix = all_tables[499, :, :, 0]
    plot_matrix(matrix)


def plot_matrix(matrix, **kwargs):
    """
    Shows a plot of the given matrix. Extra keyword arguments are passed on to
    matshow.
    """
    if 'cmap' not in kwargs:
        kwargs['cmap'] = "RdBu"
    _, axis = plt.subplots()
    axis.matshow(matrix, **kwargs)
    ticks = ["GP", "GPAI", "GPM", "GPAIM", "AI", "BL", "BLH"]
    plt.xticks(range(7), ticks)
    plt.yticks(range(7), ticks)
    for (i, j), value in np.ndenumerate(matrix):
        axis.text(j, i, '{:.3f}'.format(value), ha='center', va='center')
    plt.show()


def open_lines(filename):
    """
    Takes in a filename and returns a list of all the lines of the file,
    starting from the second line.
    """
    with open(filename) as file:
        data = file.read()
    data = data.splitlines()
    data = data[1:]
    return data


def get_values(values):
    """
    Converts a string in the format "(f1;f2)" into a list of floats [f1, f2].
    """
    as_floats = [float(v) for v in re.findall(r"\d+\.\d+", values)]
    return as_floats


def line_to_table(line):
    """
    Converts a line of the file into a 3D numpy array.
    """
    rows = [r for r in re.split(r",\]+|\[+", line) if r is not '']
    table = [re.split(r",", r) for r in rows]
    value_table = [[get_values(values) for values in row] for row in table]
    return np.array(value_table)


__main__()
