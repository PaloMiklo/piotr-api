package com.api.piotr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.api.piotr.entity.ImageTable;
import com.api.piotr.repository.ImageRepository;

@SpringBootApplication
public class PiotrApplication implements CommandLineRunner {

	@Autowired
	private ImageRepository imageRepository;

	public static void main(String[] args) {
		SpringApplication.run(PiotrApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws IOException {
		// persist images as bytea on mocked data
		imageRepository.updateAll(findAllImages().stream()
				.map(this::updateImage)
				.collect(Collectors.toList()));
	}

	private List<ImageTable> findAllImages() {
		return imageRepository.findAllById(Set.of(1L, 2L, 3L));
	}

	private ImageTable updateImage(ImageTable image) {
		image.setImage(getImageBytes(image));
		return image;
	}

	private byte[] getImageBytes(ImageTable e) {
		try {
			return Files.readAllBytes(Paths.get(String.format("images/product%s.jpg", e.getId())));
		} catch (IOException ex) {
			throw new RuntimeException("Error during extracting bytes from an image", ex);
		}
	}
}
