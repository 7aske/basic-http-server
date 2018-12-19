package tests;

import java.nio.file.Path;
import java.util.function.Predicate;

class IsValidPath implements Predicate<Path> {
	@Override
	public boolean test(Path path) {
		return false;
	}

	@Override
	public Predicate<Path> and(Predicate<? super Path> other) {
		return null;
	}

	@Override
	public Predicate<Path> negate() {
		return null;
	}

	@Override
	public Predicate<Path> or(Predicate<? super Path> other) {
		return null;
	}
}
