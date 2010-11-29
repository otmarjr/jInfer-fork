CPP=/usr/bin/cpp -P -w -C

.PHONY: images photos style

all: about.html doc_developer.html doc_tutorial.html documentation.html doc_user.html download.html empty.html footer.html header.html help.html index.html legal.html images photos style

IMGS= jinfer-logo.png document-save-2.png edit-find-4.png configure-4.png help-hint.png background3.png box-downleft.png
PHTS= anti.jpg reseto.jpg rio.jpg sviro.jpg vektor.jpg

images:
	mkdir -p output/images
	for i in $(IMGS); do cp images/$$i output/images/$$i; done

photos:
	mkdir -p output/photos
	for i in $(PHTS); do cp photos/$$i output/photos/$$i; done

style:
	cp style.css output/style.css

%.html: %.html_
	$(CPP) $< > output/$@


clean:
	rm -fr output/*
