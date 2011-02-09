obj= architecture base basicdtd basicigg basicruledisplayer basicxsd runner twostep treeruledisplayer

all: deploy

deploy: subdirs
	mkdir -p ../../project-web/output/modules/;
	for i in $(obj); do cp $$i/$$i.pdf ../../project-web/output/modules/; done

subdirs:
	for i in $(obj); do cd $$i && $(MAKE) && cd ..; done

clean:
	for i in $(obj); do cd $$i && $(MAKE) clean && cd ..; done
