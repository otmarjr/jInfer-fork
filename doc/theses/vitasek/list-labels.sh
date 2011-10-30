#!/bin/bash

cat *.tex | grep "\\\\label" | sed "s/^.*\\label{\([^}]*\)}.*$/\1/" | sort

echo "REFS: "

cat *.tex | grep "\\\\ref" | sed "s/^.*\\ref{\([^}]*\)}.*$/\1/" | sort