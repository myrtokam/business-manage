import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            FileReader fr = new FileReader("DATA/CUSTOMERS.CSV");
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String lines = br.readLine();

            // Μεταβλητές
            int max_deposit = 0, count_deposit = 0, max_age = 35;
            double sum_deposits = 0;
            double avg_deposit = 0;
            String best_customer = " ";
            String age_of_customer = " ";

            // Μετρητές ηλικιών
            int customers_above_25 = 0;
            int customers_above_35 = 0;
            int customers_above_45 = 0;
            int customers_above_55 = 0;

            // Νέες λίστες για πληρη ανάλυση
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Float> total_spents = new ArrayList<>();
            ArrayList<String> cities = new ArrayList<>();
            ArrayList<String> payment_methods = new ArrayList<>();
            ArrayList<String> sources = new ArrayList<>();
            ArrayList<Integer> customer_scores = new ArrayList<>();

            while (lines != null) {
                String[] columns = lines.split(",");

                int deposit = Integer.parseInt(columns[7]);
                String name_customer = columns[0];
                String lastname_customer = columns[1];
                int age = Integer.parseInt(columns[2]);
                String city = columns[6];
                String payment_method = columns[11];
                String source = columns[14];
                int customer_score = Integer.parseInt(columns[20]);
                float total_spent = Float.parseFloat(columns[21]);

                String full_name = name_customer + " " + lastname_customer;

                // Αποθήκευση σε λίστες
                names.add(full_name);
                total_spents.add(total_spent);
                cities.add(city);
                payment_methods.add(payment_method);
                sources.add(source);
                customer_scores.add(customer_score);

                // Καλύτερος πελάτης (deposit)
                if (deposit > max_deposit) {
                    max_deposit = deposit;
                    best_customer = full_name;
                } else if (deposit == max_deposit) {
                    best_customer += " , " + full_name;
                }

                sum_deposits += deposit;
                count_deposit++;

                // Μετρητές ηλικιών
                if (age > 25) customers_above_25++;
                if (age > 35) customers_above_35++;
                if (age > 45) customers_above_45++;
                if (age > 55) customers_above_55++;

                // Πελάτες > 35 (για εμφάνιση ονομάτων)
                if (age > max_age) {
                    max_age = age;
                    age_of_customer = full_name;
                } else if (age == max_age) {
                    age_of_customer += " , " + full_name;
                }

                lines = br.readLine();
            }

            br.close();
            fr.close();

            // === ΥΠΟΛΟΓΙΣΜΟΙ ===

            // 1. Μέσος όρος deposits
            avg_deposit = sum_deposits / count_deposit;

            // 2. Συνολικά έσοδα από πελάτες
            float total_revenue_customers = 0;
            for (float spent : total_spents) {
                total_revenue_customers += spent;
            }

            // 3. Μέσος όρος total_spent
            float avg_total_spent = total_revenue_customers / total_spents.size();

            // 4. Πελάτες ανά πόλη
            int athens = 0, thessaloniki = 0, other_cities = 0;
            for (String city : cities) {
                if (city.equals("Athens")) athens++;
                else if (city.equals("Thessaloniki")) thessaloniki++;
                else other_cities++;
            }

            // 5. Πελάτες ανά τρόπο πληρωμής
            int card_payments = 0, cash_payments = 0;
            for (String method : payment_methods) {
                if (method.equals("card")) card_payments++;
                else if (method.equals("cash")) cash_payments++;
            }

            // 6. Πελάτες ανά source
            int store_customers = 0, online_customers = 0;
            for (String src : sources) {
                if (src.equals("store")) store_customers++;
                else if (src.equals("online")) online_customers++;
            }

            // 7. Average customer score
            float total_score = 0;
            for (int score : customer_scores) {
                total_score += score;
            }
            float avg_customer_score = total_score / customer_scores.size();

            // 8. Top 5 πελάτες
            ArrayList<Float> temp_spents = new ArrayList<>(total_spents);
            ArrayList<String> top5_customers = new ArrayList<>();
            ArrayList<Float> top5_amounts = new ArrayList<>();

            for (int i = 0; i < Math.min(5, names.size()); i++) {
                float max_spent = 0;
                int max_idx = 0;
                for (int j = 0; j < temp_spents.size(); j++) {
                    if (temp_spents.get(j) > max_spent) {
                        max_spent = temp_spents.get(j);
                        max_idx = j;
                    }
                }
                top5_customers.add(names.get(max_idx));
                top5_amounts.add(max_spent);
                temp_spents.set(max_idx, -1f);
            }

            // === ΕΜΦΑΝΙΣΗ ΑΠΟΤΕΛΕΣΜΑΤΩΝ CUSTOMERS ===
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║       CUSTOMERS STATISTICS - FULL REPORT           ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            System.out.println("📊 GENERAL:");
            System.out.println("   Total Customers: " + names.size());
            System.out.println("   Total Revenue: €" + String.format("%.2f", total_revenue_customers));
            System.out.println();

            System.out.println("💰 DEPOSITS:");
            System.out.println("   Best Customer (by deposit): " + best_customer);
            System.out.println("   Max Deposit: €" + max_deposit);
            System.out.println("   Average Deposit: €" + String.format("%.2f", avg_deposit));
            System.out.println();

            System.out.println("💳 SPENDING:");
            System.out.println("   Average Total Spent: €" + String.format("%.2f", avg_total_spent));
            System.out.println();

            System.out.println("👤 DEMOGRAPHICS:");
            System.out.println("   Customers > 35 years: " + age_of_customer);
            System.out.println();

            System.out.println("👥 AGE GROUPS:");
            System.out.println("   Customers > 25 years: " + customers_above_25);
            System.out.println("   Customers > 35 years: " + customers_above_35);
            System.out.println("   Customers > 45 years: " + customers_above_45);
            System.out.println("   Customers > 55 years: " + customers_above_55);
            System.out.println();

            System.out.println("🏙️  CITIES:");
            System.out.println("   Athens: " + athens);
            System.out.println("   Thessaloniki: " + thessaloniki);
            System.out.println("   Other: " + other_cities);
            System.out.println();

            System.out.println("💳 PAYMENT METHODS:");
            System.out.println("   Card: " + card_payments);
            System.out.println("   Cash: " + cash_payments);
            System.out.println();

            System.out.println("🛒 SOURCE:");
            System.out.println("   Store: " + store_customers);
            System.out.println("   Online: " + online_customers);
            System.out.println();

            System.out.println("⭐ SATISFACTION:");
            System.out.println("   Average Customer Score: " + String.format("%.1f", avg_customer_score) + "/100");
            System.out.println();

            System.out.println("🏆 TOP 5 CUSTOMERS (by total spent):");
            for (int i = 0; i < top5_customers.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + top5_customers.get(i) +
                        " - €" + String.format("%.2f", top5_amounts.get(i)));
            }

            // Κλήση της product()
            product();

            // Κλήση της employees()
            employees();

            // Κλήση της finances()
            finances();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void product() throws IOException {

        try {
            FileReader fr = new FileReader("DATA/PRODUCTS.CSV");
            BufferedReader br = new BufferedReader(fr);

            br.readLine();
            String line = br.readLine();

            int count = 0;
            float MAX = 0;
            double sum = 0;
            double AVG = 0;
            String mesosOros = " ";

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Float> prices = new ArrayList<>();
            ArrayList<Float> costs = new ArrayList<>();
            ArrayList<Integer> stocks = new ArrayList<>();
            ArrayList<Integer> min_stocks = new ArrayList<>();
            ArrayList<String> categories = new ArrayList<>();
            ArrayList<Integer> units_sold = new ArrayList<>();
            ArrayList<Float> revenues = new ArrayList<>();

            while (line != null) {
                String[] columns = line.split(",");

                String name = columns[1];
                String category = columns[2];
                float price = Float.parseFloat(columns[6]);
                float cost = Float.parseFloat(columns[7]);
                int stock = Integer.parseInt(columns[8]);
                int min_stock = Integer.parseInt(columns[9]);
                int units = Integer.parseInt(columns[29]);
                float revenue = Float.parseFloat(columns[30]);

                names.add(name);
                prices.add(price);
                costs.add(cost);
                stocks.add(stock);
                min_stocks.add(min_stock);
                categories.add(category);
                units_sold.add(units);
                revenues.add(revenue);

                count++;
                sum += price;

                if (price > MAX) {
                    MAX = price;
                    mesosOros = name;
                }

                line = br.readLine();
            }

            br.close();
            fr.close();

            AVG = sum / count;

            float total_revenue = 0;
            for (float rev : revenues) {
                total_revenue += rev;
            }

            int max_sold = 0;
            String best_seller = "";
            for (int i = 0; i < units_sold.size(); i++) {
                if (units_sold.get(i) > max_sold) {
                    max_sold = units_sold.get(i);
                    best_seller = names.get(i);
                }
            }

            ArrayList<String> low_stock = new ArrayList<>();
            for (int i = 0; i < stocks.size(); i++) {
                if (stocks.get(i) < min_stocks.get(i)) {
                    low_stock.add(names.get(i) + " (Stock: " + stocks.get(i) + ", Min: " + min_stocks.get(i) + ")");
                }
            }

            float total_profit = 0;
            for (int i = 0; i < prices.size(); i++) {
                float profit_per_unit = prices.get(i) - costs.get(i);
                float product_profit = profit_per_unit * units_sold.get(i);
                total_profit += product_profit;
            }

            int electronics = 0, accessories = 0, furniture = 0, audio = 0, wearables = 0,
                    office = 0, photography = 0, networking = 0, storage = 0, others = 0;
            for (String cat : categories) {
                switch (cat) {
                    case "Electronics":
                        electronics++;
                        break;
                    case "Accessories":
                        accessories++;
                        break;
                    case "Furniture":
                        furniture++;
                        break;
                    case "Audio":
                        audio++;
                        break;
                    case "Wearables":
                        wearables++;
                        break;
                    case "Office":
                        office++;
                        break;
                    case "Photography":
                        photography++;
                        break;
                    case "Networking":
                        networking++;
                        break;
                    case "Storage":
                        storage++;
                        break;
                    default:
                        others++;
                }
            }

            ArrayList<Float> temp_revenues = new ArrayList<>(revenues);
            ArrayList<String> top5_names = new ArrayList<>();
            ArrayList<Float> top5_revenues = new ArrayList<>();

            for (int i = 0; i < Math.min(5, names.size()); i++) {
                float max_rev = 0;
                int max_idx = 0;
                for (int j = 0; j < temp_revenues.size(); j++) {
                    if (temp_revenues.get(j) > max_rev) {
                        max_rev = temp_revenues.get(j);
                        max_idx = j;
                    }
                }
                top5_names.add(names.get(max_idx));
                top5_revenues.add(max_rev);
                temp_revenues.set(max_idx, -1f);
            }

            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║       PRODUCTS STATISTICS - FULL REPORT            ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            System.out.println("📊 GENERAL:");
            System.out.println("   Total Products: " + count);
            System.out.println("   Average Price: €" + String.format("%.2f", AVG));
            System.out.println();

            System.out.println("💰 PRICING:");
            System.out.println("   Most Expensive: " + mesosOros + " (€" + String.format("%.2f", MAX) + ")");
            System.out.println();

            System.out.println("💵 REVENUE & PROFIT:");
            System.out.println("   Total Revenue: €" + String.format("%.2f", total_revenue));
            System.out.println("   Total Profit: €" + String.format("%.2f", total_profit));
            System.out.println();

            System.out.println("🏆 BEST-SELLER:");
            System.out.println("   " + best_seller + " (" + max_sold + " units sold)");
            System.out.println();

            System.out.println("📦 INVENTORY:");
            if (low_stock.isEmpty()) {
                System.out.println("   ✓ All products have sufficient stock!");
            } else {
                System.out.println("   ⚠️  Low stock products:");
                for (String product : low_stock) {
                    System.out.println("      " + product);
                }
            }
            System.out.println();

            System.out.println("📂 CATEGORIES:");
            System.out.println("   Electronics:  " + electronics);
            System.out.println("   Accessories:  " + accessories);
            System.out.println("   Furniture:    " + furniture);
            System.out.println("   Audio:        " + audio);
            System.out.println("   Wearables:    " + wearables);
            System.out.println("   Office:       " + office);
            System.out.println("   Photography:  " + photography);
            System.out.println("   Networking:   " + networking);
            System.out.println("   Storage:      " + storage);
            System.out.println("   Others:       " + others);
            System.out.println();

            System.out.println("🔝 TOP 5 PRODUCTS (by revenue):");
            for (int i = 0; i < top5_names.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + top5_names.get(i) +
                        " - €" + String.format("%.2f", top5_revenues.get(i)));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void employees() throws IOException {

        try {
            FileReader fr = new FileReader("DATA/EMPLOYEES.CSV");
            BufferedReader br = new BufferedReader(fr);

            br.readLine(); // Skip headers
            String line = br.readLine();

            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> positions = new ArrayList<>();
            ArrayList<String> departments = new ArrayList<>();
            ArrayList<Float> salaries = new ArrayList<>();
            ArrayList<Integer> performance_scores = new ArrayList<>();
            ArrayList<Integer> projects = new ArrayList<>();

            while (line != null) {
                String[] cols = line.split(",");

                String full_name = cols[1] + " " + cols[2];
                String position = cols[3];
                String department = cols[4];
                float salary = Float.parseFloat(cols[6]);
                int performance = Integer.parseInt(cols[9]);
                int proj = Integer.parseInt(cols[10]);

                names.add(full_name);
                positions.add(position);
                departments.add(department);
                salaries.add(salary);
                performance_scores.add(performance);
                projects.add(proj);

                line = br.readLine();
            }

            br.close();
            fr.close();

            // Υπολογισμοί
            float total_salaries = 0;
            for (float sal : salaries) {
                total_salaries += sal;
            }
            float avg_salary = total_salaries / salaries.size();

            int max_performance = 0;
            String best_employee = "";
            for (int i = 0; i < performance_scores.size(); i++) {
                if (performance_scores.get(i) > max_performance) {
                    max_performance = performance_scores.get(i);
                    best_employee = names.get(i);
                }
            }

            int total_projects = 0;
            for (int proj : projects) {
                total_projects += proj;
            }
            float avg_projects = (float) total_projects / projects.size();

            // Departments
            int sales = 0, it = 0, marketing = 0, management = 0, hr = 0, finance = 0, customer_service = 0;
            for (String dept : departments) {
                switch (dept) {
                    case "Sales":
                        sales++;
                        break;
                    case "IT":
                        it++;
                        break;
                    case "Marketing":
                        marketing++;
                        break;
                    case "Management":
                        management++;
                        break;
                    case "HR":
                        hr++;
                        break;
                    case "Finance":
                        finance++;
                        break;
                    case "Customer Service":
                        customer_service++;
                        break;
                }
            }

            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║       EMPLOYEES STATISTICS - FULL REPORT           ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            System.out.println("👥 GENERAL:");
            System.out.println("   Total Employees: " + names.size());
            System.out.println("   Total Monthly Salaries: €" + String.format("%.2f", total_salaries));
            System.out.println("   Average Salary: €" + String.format("%.2f", avg_salary));
            System.out.println();

            System.out.println("🏆 PERFORMANCE:");
            System.out.println("   Best Employee: " + best_employee + " (Score: " + max_performance + ")");
            System.out.println("   Total Projects Completed: " + total_projects);
            System.out.println("   Average Projects per Employee: " + String.format("%.1f", avg_projects));
            System.out.println();

            System.out.println("🏢 DEPARTMENTS:");
            System.out.println("   Management:       " + management);
            System.out.println("   Sales:            " + sales);
            System.out.println("   Marketing:        " + marketing);
            System.out.println("   IT:               " + it);
            System.out.println("   HR:               " + hr);
            System.out.println("   Finance:          " + finance);
            System.out.println("   Customer Service: " + customer_service);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void finances() throws IOException {

        try {
            FileReader fr = new FileReader("DATA/FINANCES.CSV");
            BufferedReader br = new BufferedReader(fr);

            br.readLine(); // Skip headers
            String line = br.readLine();

            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> categories = new ArrayList<>();
            ArrayList<Float> amounts = new ArrayList<>();
            ArrayList<String> departments = new ArrayList<>();

            while (line != null) {
                String[] cols = line.split(",");

                String type = cols[2];
                String category = cols[3];
                float amount = Float.parseFloat(cols[5]);
                String department = cols[6];

                types.add(type);
                categories.add(category);
                amounts.add(amount);
                departments.add(department);

                line = br.readLine();
            }

            br.close();
            fr.close();

            // Υπολογισμοί
            float total_income = 0;
            float total_expenses = 0;

            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).equals("income")) {
                    total_income += amounts.get(i);
                } else if (types.get(i).equals("expense")) {
                    total_expenses += amounts.get(i);
                }
            }

            float net_profit = total_income - total_expenses;
            float profit_margin = (net_profit / total_income) * 100;

            // Expenses by category
            float salaries_expense = 0, rent_expense = 0, marketing_expense = 0, utilities_expense = 0, other_expense = 0;
            for (int i = 0; i < categories.size(); i++) {
                if (types.get(i).equals("expense")) {
                    switch (categories.get(i)) {
                        case "salaries":
                            salaries_expense += amounts.get(i);
                            break;
                        case "rent":
                            rent_expense += amounts.get(i);
                            break;
                        case "marketing":
                            marketing_expense += amounts.get(i);
                            break;
                        case "utilities":
                            utilities_expense += amounts.get(i);
                            break;
                        default:
                            other_expense += amounts.get(i);
                    }
                }
            }

            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║       FINANCES STATISTICS - FULL REPORT            ║");
            System.out.println("╚════════════════════════════════════════════════════╝\n");

            System.out.println("💰 SUMMARY:");
            System.out.println("   Total Income:   €" + String.format("%.2f", total_income));
            System.out.println("   Total Expenses: €" + String.format("%.2f", total_expenses));
            System.out.println("   Net Profit:     €" + String.format("%.2f", net_profit));
            System.out.println("   Profit Margin:  " + String.format("%.1f", profit_margin) + "%");
            System.out.println();

            System.out.println("📊 EXPENSES BY CATEGORY:");
            System.out.println("   Salaries:   €" + String.format("%.2f", salaries_expense));
            System.out.println("   Rent:       €" + String.format("%.2f", rent_expense));
            System.out.println("   Marketing:  €" + String.format("%.2f", marketing_expense));
            System.out.println("   Utilities:  €" + String.format("%.2f", utilities_expense));
            System.out.println("   Other:      €" + String.format("%.2f", other_expense));
            System.out.println();

            if (net_profit > 0) {
                System.out.println("✅ PROFITABLE: Company is making profit!");
            } else {
                System.out.println("⚠️  WARNING: Company is operating at a loss!");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}