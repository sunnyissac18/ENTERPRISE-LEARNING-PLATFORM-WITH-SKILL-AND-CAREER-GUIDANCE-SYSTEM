import { TestBed } from '@angular/core/testing';

import { SkillProfile } from './skill-profile';

describe('SkillProfile', () => {
  let service: SkillProfile;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillProfile);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
