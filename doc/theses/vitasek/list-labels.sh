#!/bin/bash

cat *.tex | grep "\\\\label" | sed "s/^.*\\label{\([^}]*\)}.*$/\1/" | sort