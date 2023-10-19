package com.springnote.api.utils.file;

import lombok.*;

import java.io.File;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConvertedFile {
    private File file;
    private String name;
}
