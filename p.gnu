#!/usr/bin/env gnuplot

#Fichero LibroIngles

set xlabel "Terms"

set ylabel "frequency of terms"

set key box linestyle 1 lt rgb "orange"

set autoscale xy

set xtics rotate # crucial line

plot 'datosSalida/AnalizadorWhiteSpaceAnalyzerlibroIngles.epub.dat' using 1:2 title "NumberOccurrences" with linespoints

set terminal svg

set output "images/LibroIngles-occurrences-frequency-AnalyzerWhiteSpace.svg"

replot

