obj= architecture base basicdtd basicigg basicruledisplayer runner twostep

all: deploy

deploy: subdirs
	for i in $(obj); do cp $$i/$$i.pdf ../../project-web/modules/; done

subdirs:
	for i in $(obj); do cd $$i && $(MAKE) && cd ..; done

clean:
	for i in $(obj); do cd $$i && $(MAKE) clean && cd ..; done



