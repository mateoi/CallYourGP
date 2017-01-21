# -*- coding: utf-8 -*-
"""
Used to graph data gathered by the gp program
"""

import matplotlib.pyplot as plt

BASE_LOCATION = "D:/Documents/workspace/CallYourGP/data/ski/"
FILENAME_PATTERN = "{}_{}{}_{}.csv"


def __main__():
    average_fitness_00 = get_average_data('max_fitness')
    average_fitness_10 = get_average_data('max_fitness', True)
    average_fitness_01 = get_average_data('max_fitness', False, True)
    average_fitness_11 = get_average_data('max_fitness', True, True)
    generations = range(500)
    plt.plot(generations, average_fitness_00, 'r',
             generations, average_fitness_10, 'b',
             generations, average_fitness_01, 'g',
             generations, average_fitness_11, 'k')
    plt.ylabel("Average fitness")
    plt.xlabel("Generation")
    plt.grid(True)
    plt.show()


def get_average_data(key, has_memory=False, has_ai=False, game="Ski"):
    """
    Reads from files with the given properties and returns the average values
    for each generation for the given key
    """
    fnames = [create_filename(i, has_memory, has_ai, game) for i in range(10)]
    data = [open_lines(file) for file in fnames]
    data = [[line_to_dictionary(gen) for gen in item] for item in data]
    return average_data(data, key)


def create_filename(index, has_memory=False, has_ai=False, game="Ski"):
    """
    Creates a filename for the data given the dataset required
    """
    memory = 1 if has_memory else 0
    ai_available = 1 if has_ai else 0
    filename = FILENAME_PATTERN.format(game, memory, ai_available, index)
    return BASE_LOCATION+filename


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
    values = [[gen[key] for gen in item] for item in data]
    sums = [sum([value[i] for value in values]) for i in range(len(values[0]))]
    averages = [value / len(values) for value in sums]
    return averages


__main__()
