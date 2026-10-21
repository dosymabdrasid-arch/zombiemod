# Zombie Apocalypse (Fabric, Minecraft 26.3)

## Как получить готовый .jar (без установки Java)
1. Создайте аккаунт на github.com и новый репозиторий (New repository).
2. Загрузите в него ВСЕ файлы из этой папки (Add file → Upload files; папку `.github` тоже обязательно).
3. Откройте вкладку **Actions** → **Build mod** → дождитесь зелёной галочки.
4. Внутри запуска скачайте **zombiemod-jar** → в архиве файл `zombiemod-1.0.0.jar` (НЕ `-sources`).
5. Если сборка красная — откройте её, скопируйте текст ошибки и отправьте Claude.

## Установка в Legacy Launcher
1. В лаунчере выберите версию **Fabric 26.3** (Fabric Loader 0.19.5+).
2. В папку `mods` положите: `zombiemod-1.0.0.jar` и **Fabric API 0.161.0+26.3** (modrinth.com/mod/fabric-api).
3. Нужна Java 25 (лаунчер обычно ставит сам).

## Что внутри
Зомби: Бегун, Токсичный (яд), Громила (танк), Тиран (босс). Не горят на солнце, дропают патроны и бинты.
Оружие: бита, мачете, пистолет, дробовик, автомат (ПКМ — выстрел, патроны берутся из инвентаря).
Предметы: патрон, патрон для дробовика, бинт (лечит). Всё крафтится, есть во вкладке «Бой».
Призвать вручную: /summon zombiemod:tyrant
Баланс — в файле Variant.java и ModItems.java.

## Если оружие в руке повёрнуто криво
Файлы src/main/resources/assets/zombiemod/models/item/pistol.json (rifle, shotgun) → блок "display" →
"firstperson_righthand"/"thirdperson_righthand": поменяйте rotation и translation.
