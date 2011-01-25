#!/bin/bash

find . -name "*.tex" -exec echo "===================== {} :" \; -exec grep -i todo {} \;