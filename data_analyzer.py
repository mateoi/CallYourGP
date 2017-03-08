# -*- coding: utf-8 -*-
"""
Used to graph data gathered by the gp program
"""

import matplotlib.pyplot as plt
import numpy as np


BASE_LOCATION = "D:/Documents/workspace/CallYourGP/data/ski/"
FILENAME_PATTERN = "Ski_ct_{}{}_{}.csv"
AI_FILENAME = "AI_fitness.txt"


def __main__():
    make_plot()


def make_plot():
    """
    Makes a plot of the Ski data.
    """
    data_00 = get_data(False, False)
    data_10 = get_data(True, False)
    data_01 = get_data(False, True)
    data_11 = get_data(True, True)
    generations = range(500)
    plt.plot(generations, average_data(data_00, 'max_fitness'), 'r', label="GP")
    plt.plot(generations, average_data(data_10, 'max_fitness'), 'b', label="GPM")
    plt.plot(generations, average_data(data_01, 'max_fitness'), 'g', label="GPAI")
    plt.plot(generations, average_data(data_11, 'max_fitness'), 'k', label="GPAIM")
    legend = plt.legend(bbox_to_anchor=(0, 1.02, 1, 0.1), loc=3, ncol=5,
                        mode="expand", borderaxespad=0.)
    for legend_object in legend.legendHandles:
        legend_object.set_linewidth(4.0)
    plt.title("Maximum fitness for all Ski variants", y=1.1)
    plt.ylabel("Maximum fitness")
    plt.xlabel("Generation")

    plt.grid(True)
    plt.show()


def node_sizes_per_file(filename):
    """
    Returns a list of the size of the node in each line of a given Pong file.
    """
    lines = open_lines(filename)
    return [line.count('(') for line in lines]


def get_average_node_sizes(has_memory, has_ai):
    fnames = [create_filename(i, has_memory, has_ai) for i in range(10)]
    sizes = np.array([node_sizes_per_file(fname) for fname in fnames])
    return sizes.mean(axis=0)


def get_best_node(data, key='max_fitness'):
    """
    Given an array of dictionaries, returns a string representation of the
    node that has the highest fitness.
    """
    values = np.array([[gen[key] for gen in item] for item in data])
    flat_index = values.argmax()
    coords = np.unravel_index(flat_index, data.shape)
    return data[coords]['max_node']


def get_data(has_memory=False, has_ai=False):
    """
    Reads from files with the given properties and returns the values for each
    generation.
    """
    fnames = [create_filename(i, has_memory, has_ai) for i in range(10)]
    data = [open_lines(file) for file in fnames]
    data = [[line_to_dictionary(gen) for gen in item] for item in data]
    return np.array(data)


def get_node_size(has_memory=False, has_ai=False):
    """
    Reads from files with the given properties and returns the size of the best
    node for each generation.
    """
    fnames = [create_filename(i, has_memory, has_ai) for i in range(10)]
    data = [open_lines(file) for file in fnames]
    data = [[gen.count('(') for gen in item] for item in data]
    return np.array(data)


def create_filename(index, has_memory=False, has_ai=False):
    """
    Creates a filename for the data given the dataset required
    """
    memory = 1 if has_memory else 0
    ai_available = 1 if has_ai else 0
    filename = FILENAME_PATTERN.format(memory, ai_available, index)
    return BASE_LOCATION + filename


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


def line_to_dictionary(line):
    """
    Takes in a line from the file and converts it into a dictionary containing
    the items of the line.
    """
    as_list = line.split(',')
    return {"generation": int(as_list[0]), "avg_fitness": float(as_list[1]),
            "max_fitness": float(as_list[2]), "max_node": as_list[3]}


def average_data(data, key):
    """
    Given a list of lists of dictionaries, gets the values contained in the
    given key in the dictionaries and averages them across all the lists.
    """
    values = np.array([[gen[key] for gen in item] for item in data])
    return values.mean(axis=0)


def max_data(data, key):
    """
    Given a list of lists of dictionaries, gets the values contained in the
    given key in the dictionaries and returns the maximum across all the lists.
    """
    values = np.array([[gen[key] for gen in item] for item in data])
    return values.max(axis=0)


def get_ai_value():
    """
    Returns the average of all values in the AI fitness file
    """
    lines = open_lines(BASE_LOCATION + AI_FILENAME)
    as_floats = [float(line) for line in lines]
    return np.mean(as_floats)


__main__()
