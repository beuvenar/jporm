/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.core.session;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.JPO;
import com.jporm.core.JPOrm;
import com.jporm.validator.ValidatorService;
import com.jporm.validator.jsr303.JSR303ValidatorService;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class ValidatorServiceTest  extends BaseTestApi {

	private final ValidatorService validationService = new JSR303ValidatorService();

	@Test
	public void testBeanValidation() {
		Song song = new Song();
		song.setTitle("u"); //$NON-NLS-1$
		song.setYear(100);

		try {
			validationService.validateThrowException(song);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}
	}

	@Test
	public void testCollectionValidation() {
		Song song = new Song();
		song.setTitle("u"); //$NON-NLS-1$
		song.setYear(100);

		List<Song> songs = new ArrayList<>();
		songs.add(song);

		try {
			validationService.validateThrowException(songs);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}
	}

	@Test
	public void testJPOValidationError() {
		Song song = new Song();
		song.setTitle("u"); //$NON-NLS-1$
		song.setYear(100);

		JPO jpo = new JPOrm(new NullSessionProvider());
		jpo.setValidatorService(validationService);

		try {
			jpo.session().save(song);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}

		try {
			jpo.session().update(song);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}

		try {
			jpo.session().saveOrUpdate(song);
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (ConstraintViolationException e) {
			//ok
		}
	}

	class Song {
		private Long id;
	    private Long lyricId;

	    @NotNull(message="notNull")
	    @Size(min = 3, message="minLenght3")
	    private String title;

	    @NotNull(message="notNull")
	    @Size(min = 3, message="minLenght3")
	    private String artist;
	    //		@Size(min = 4, message="minLenght4")
	    @Min(value=1900, message="minSize1900")
	    private Integer year;

	    public Long getId() {
	        return id;
	    }

	    public void setId(final Long id) {
	        this.id = id;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(final String title) {
	        this.title = title;
	    }

	    public String getArtist() {
	        return artist;
	    }

	    public void setArtist(final String artist) {
	        this.artist = artist;
	    }

	    public Integer getYear() {
	        return year;
	    }

	    public void setYear(final Integer year) {
	        this.year = year;
	    }

	    public Long getLyricId() {
	        return lyricId;
	    }

	    public void setLyricId(final Long lyricId) {
	        this.lyricId = lyricId;
	    }

	}

}
