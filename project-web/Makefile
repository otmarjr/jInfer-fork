CPP=/usr/bin/cpp -P -w -C

all: about.html doc_developer.html doc_tutorial.html documentation.html doc_user.html download.html empty.html footer.html header.html help.html index.html legal.html

%.html: %.html_
	$(CPP) $< > $@
	mv $@ output/

clean:
	rm output/*.html
