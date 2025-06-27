package org.yearup.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api/images")
@CrossOrigin
public class ImageController {

    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        try {
            // Try to load the image from static/images directory
            Resource resource = new ClassPathResource("static/images/" + imageName);
            
            if (resource.exists()) {
                byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
                
                // Determine content type based on file extension
                String contentType = determineContentType(imageName);
                
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);
            } else {
                // Generate a placeholder image
                byte[] placeholderImage = generatePlaceholderImage(imageName);
                
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(placeholderImage);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading image: " + e.getMessage());
        }
    }

    private byte[] generatePlaceholderImage(String imageName) throws IOException {
        // Create a simple placeholder image
        int width = 400;
        int height = 400;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Set background color
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        
        // Set text color and font
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Draw placeholder text
        String text = "Image: " + imageName;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = height / 2;
        
        g2d.drawString(text, x, y);
        
        // Draw "No Image Available" text
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String noImageText = "No Image Available";
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(noImageText);
        x = (width - textWidth) / 2;
        y = height / 2 + 30;
        
        g2d.drawString(noImageText, x, y);
        
        g2d.dispose();
        
        // Convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
} 