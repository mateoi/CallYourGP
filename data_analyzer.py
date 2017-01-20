# -*- coding: utf-8 -*-
"""
Used to graph data gathered by the gp program
"""

import matplotlib.pyplot as plt

BASE_LOCATION = "D:/Documents/workspace/CallYourGP/data/ski/"
SAMPLE_FILENAME = "Ski_00_{}.csv"


def __main__():
    data = open_lines(BASE_LOCATION + SAMPLE_FILENAME.format(0))
    data = [line_to_dictionary(generation) for generation in data]
    plt.plot([generation["avg_fitness"] for generation in data])
    plt.ylabel("Average fitness")
    plt.xlabel("Generation")
    plt.grid(True)
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


def line_to_dictionary(line):
    """
    Takes in a line from the file and converts it into a dictionary containing
    the items of the line.
    """
    as_list = line.split(',')
    return {"generation": int(as_list[0]), "avg_fitness": float(as_list[1]),
            "max_fitness": float(as_list[2]), "max_node": as_list[3]}


__main__()
