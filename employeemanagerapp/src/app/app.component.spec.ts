import { TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { AppComponent } from './app.component';
import { EmployeeService } from './employee.service';

describe('AppComponent', () => {
  beforeEach(async () => {
    const employeeServiceMock = {
      getEmployees: () => of([]),
      addEmployee: () => of({} as never),
      updateEmployee: () => of({} as never),
      deleteEmployee: () => of(void 0)
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent, FormsModule],
      providers: [
        { provide: EmployeeService, useValue: employeeServiceMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
