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
    matrix = all_tables[-1, :, :, 1]
    plot_matrix(matrix, "Average paddle hits against opponent")
    # plot_running_averages(all_tables[:, :, :, 1])


def plot_running_averages(tables):
    """
    Plots the running averages of the four evolved variants in a single figure.
    """
    fig, axes = plt.subplots(2, 2, sharey="row")
    fig.add_subplot(111, frameon=False)
    plt.tick_params(labelcolor='none',
                    top='off', bottom='off', left='off', right='off')
    plt.xlabel("Generation")
    plt.ylabel("Average hits per game")
    plot_1d_array(running_average(tables[:, 0, 0]), axes[0][0], title="GP")
    plot_1d_array(running_average(tables[:, 2, 2]), axes[0][1], title="GPM")
    plot_1d_array(running_average(tables[:, 1, 1]), axes[1][0], title="GPAI")
    plot_1d_array(running_average(tables[:, 3, 3]), axes[1][1], title="GPAIM")
    plt.show()


def plot_1d_array(array, axis, baseline=None, title=None, **kwargs):
    """
    Plots the given array (and possibly a constant baseline) on the given axis.
    Extra keyword arguments are passed on to plt.plot.
    """
    axis.plot(array, **kwargs)
    if baseline is not None:
        axis.plot([baseline] * array.size)
    if title is not None:
        axis.set_title(title)


def running_average(array):
    """
    Calculates the running average of a numpy array
    """
    indices = np.arange(1, array.size + 1)
    return array.cumsum() / indices


def sliding_window_average(array, size):
    """
    Calculates the running average of size elements of a numpy array.
    Taken from:
    https://stackoverflow.com/questions/13728392/moving-average-or-running-mean
    """
    return np.convolve(array, np.ones(size) / size)[size-1:]


def plot_matrix(matrix, title, **kwargs):
    """
    Shows a plot of the given matrix. Extra keyword arguments are passed on to
    matshow.
    """
    if 'cmap' not in kwargs:
        kwargs['cmap'] = "RdBu"
    _, axis = plt.subplots()
    axis.matshow(matrix, **kwargs)
    ticks = ["GP", "GPAI", "GPM", "GPAIM", "AI", "BL", "BLH"]
    plt.title(title)
    plt.xlabel("Opponent")
    plt.ylabel("Individual")
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
