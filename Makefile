check:
	./gradlew check

publish: check
	./gradlew bintrayUpload

# use - make update-version VERSION=0.0.1
update-version:
	sed -i 's/version = .*/version = "$(VERSION)"/' build.gradle.kts

release-commit:
	[ -d ".git" ] && git add build.gradle.kts && \
	 git commit -m "Version bump to $(VERSION)" && \
	 git tag -a v$(VERSION) -m "Version $(VERSION)" && \
	 git push --follow-tags

# use - make release VERSION=0.0.1
release: update-version check release-commit publish
